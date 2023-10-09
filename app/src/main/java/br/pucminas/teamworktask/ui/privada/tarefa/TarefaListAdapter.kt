package br.pucminas.teamworktask.ui.privada.tarefa

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucminas.teamworktask.databinding.LayoutTarefaAdapterBinding
import br.pucminas.teamworktask.models.Tarefa
import br.pucminas.teamworktask.models.enums.PrioridadeEnum
import br.pucminas.teamworktask.utils.FormatterUtils

class TarefaListAdapter(val context: Context, private val listaTarefa: List<Tarefa>) :
    RecyclerView.Adapter<TarefaListAdapter.ViewHolder>() {
    private lateinit var binding: LayoutTarefaAdapterBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Tarefa) {
            binding.apply {
                tarefaNomeTv.text = item.nome
                tarefaDescricaoTv.text = item.descricao
                tarefaPrioridadeTv.text = PrioridadeEnum.getPrioridadeEnumById(item.prioridade.toInt()).nomePrioridade
                if(item.dataEntrega != null){
                    tarefaDataTv.text = FormatterUtils.formatDateToString(item.dataEntrega!!)
                }
                if(item.tag != null){
                    tarefaCard.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.tag!!.cor))
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
        holder.bind(item = listaTarefa[position])
    }
}