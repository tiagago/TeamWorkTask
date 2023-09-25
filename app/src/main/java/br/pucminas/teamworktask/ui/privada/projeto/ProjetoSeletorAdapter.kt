package br.pucminas.teamworktask.ui.privada.projeto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.LayoutProjetoAdapterBinding
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.utils.FormatterUtils

class ProjetoSeletorAdapter(val context: Context, private val listaProjetos: List<Projeto>, private val projetoSelecionado: Projeto, private val projetoSeletorOnClickInterface: ProjetoSeletorOnClickInterface) :
    RecyclerView.Adapter<ProjetoSeletorAdapter.ViewHolder>() {
    private lateinit var binding: LayoutProjetoAdapterBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Projeto) {
            binding.apply {
                if(item.id == projetoSelecionado.id) {
                    projetoCard.backgroundTintList =
                        context.getColorStateList(R.color.primary_color_light)
                }
                projetoCardArrowIv.visibility = View.GONE
                projetoCardNomeTv.text = item.nome
                projetoCardDescricaoTv.text = item.descricao
                projetoCardDataCriacaoTv.text = FormatterUtils.formatDateToString(item.dataCriacao)

                root.setOnClickListener {
                    projetoSeletorOnClickInterface.onClickProjeto(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutProjetoAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = listaProjetos.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = listaProjetos[position])
    }
}