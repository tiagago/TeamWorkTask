package br.pucminas.teamworktask.ui.privada.equipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.LayoutEquipeAdapterBinding
import br.pucminas.teamworktask.models.Usuario

class EquipeParticipantesAdapter(val context: Context, private val listaParticipantes: List<Usuario>, private val isCriador: Boolean, private val equipeParticipanteOnClickInterface: EquipeParticipanteOnClickInterface) :
    RecyclerView.Adapter<EquipeParticipantesAdapter.ViewHolder>() {
    private lateinit var binding: LayoutEquipeAdapterBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Usuario) {
            binding.apply {
                if(listaParticipantes[0].id == item.id) {
                    equipeParticipanteDeleteTv.visibility = View.GONE
                    equipeParticipanteTv.text = context.getString(R.string.equipe_lista_nome_criador, item.nomeExibicao)
                } else {
                    equipeParticipanteTv.text = item.nomeExibicao
                    equipeParticipanteDeleteTv.visibility = if(isCriador) View.VISIBLE else View.GONE
                }
                equipeParticipanteDeleteTv.setOnClickListener {
                    equipeParticipanteOnClickInterface.onClickExcluirParticipante(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutEquipeAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = listaParticipantes.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = listaParticipantes[position])
    }
}