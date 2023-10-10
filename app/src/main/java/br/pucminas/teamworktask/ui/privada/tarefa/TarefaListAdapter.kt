package br.pucminas.teamworktask.ui.privada.tarefa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.InsetDrawable
import android.provider.CalendarContract
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.LayoutTarefaAdapterBinding
import br.pucminas.teamworktask.models.Tarefa
import br.pucminas.teamworktask.models.enums.PrioridadeEnum
import br.pucminas.teamworktask.utils.FormatterUtils

class TarefaListAdapter(val context: Context, private val listaTarefa: List<Tarefa>, private val onclickInterface: TarefaItemListOnClickInterface) :
    RecyclerView.Adapter<TarefaListAdapter.ViewHolder>() {
    private lateinit var binding: LayoutTarefaAdapterBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tarefa: Tarefa) {
            binding.apply {
                tarefaNomeTv.text = tarefa.nome
                tarefaDescricaoTv.text = tarefa.descricao
                tarefaPrioridadeTv.text = PrioridadeEnum.getPrioridadeEnumById(tarefa.prioridade.toInt()).nomePrioridade
                if(tarefa.dataEntrega != null){
                    tarefaDataTv.text = FormatterUtils.formatDateToString(tarefa.dataEntrega!!)
                }
                if(tarefa.tag != null){
                    tarefaCard.backgroundTintList = ColorStateList.valueOf(Color.parseColor(tarefa.tag!!.cor))
                }
                tarefaMore.setOnClickListener {
                    showMenu(it, R.menu.pop_menu, tarefa)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutTarefaAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = listaTarefa.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tarefa = listaTarefa[position])
    }
    /******************************************
     **** Chamar o menu de seleção de ação ****
     ******************************************/
    @SuppressLint("RestrictedApi")
    private fun showMenu(v: View, @MenuRes menuRes: Int, tarefa: Tarefa) {
        val popup = PopupMenu(context!!, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            if(menuItem.title == context.getString(R.string.tarefas_card_pop_agendar_label)){
                chamarAgenda(tarefa)
            } else if (menuItem.title == context.getString(R.string.tarefas_card_pop_deletar_label)){
                onclickInterface.onClickExcluirTarefa(tarefa)
            } else {
                onclickInterface.onClickEditarTarefa(tarefa)
            }

            return@setOnMenuItemClickListener true
        }

        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 2.0f, context.resources.displayMetrics)
                        .toInt()
                if (item.icon != null) {
                    item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx,0)
                    (item.icon as InsetDrawable).setTint(context.getColor(R.color.secondary_color))
                }
            }
        }

        // Show the popup menu.
        popup.show()
    }

    /*********************************
     **** Chamar Intent da agenda ****
     *********************************/
    fun chamarAgenda(tarefa: Tarefa){
        val calID: Long = 3

        if(tarefa.dataEntrega != null){
            val startMillis: Long = tarefa.dataEntrega!!.time
            val endMillis: Long = tarefa.dataEntrega!!.time

            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                .putExtra(CalendarContract.Events.TITLE, context.getString(R.string.tarefas_agenda_titulo, tarefa.nome))
                .putExtra(CalendarContract.Events.DESCRIPTION, tarefa.descricao)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
            context.startActivity(intent)
        }

    }

}