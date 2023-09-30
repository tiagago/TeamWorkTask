package br.pucminas.teamworktask.ui.privada.tags

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucminas.teamworktask.databinding.LayoutTagAdapterBinding
import br.pucminas.teamworktask.models.Tag

class TagListAdapter(val context: Context, private val listaTag: List<Tag>, private val tagItemListOnClickInterface: TagItemListOnClickInterface) :
    RecyclerView.Adapter<TagListAdapter.ViewHolder>() {
    private lateinit var binding: LayoutTagAdapterBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Tag) {
            binding.apply {
                tagListaBgIv.setColorFilter(Color.parseColor(item.cor))
                tagListaNomeTv.text = item.nome
                tagListaCl.setOnClickListener {
                    tagItemListOnClickInterface.onClickEditarTag(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutTagAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = listaTag.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = listaTag[position])
    }
}