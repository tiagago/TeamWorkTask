package br.pucminas.teamworktask.ui.privada.projeto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.LayoutHistoricoAdapterBinding
import br.pucminas.teamworktask.models.Historico
import br.pucminas.teamworktask.utils.FormatterUtils

class HistoricoAdapter(val context: Context, private val historicos: List<Historico>) :
    RecyclerView.Adapter<HistoricoAdapter.ViewHolder>() {
    private lateinit var binding: LayoutHistoricoAdapterBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Historico) {
            binding.apply {
                historicoDataTv.text = FormatterUtils.formatDateToStringWithHour(item.dataCriacao)
                historicoDescricaoTv.text = context.getString(R.string.dashboard_historico_informacao, item.usuario.nomeExibicao , item.descricao)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutHistoricoAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = historicos.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = historicos[position])
    }
}