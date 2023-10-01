package br.pucminas.teamworktask.ui.privada.tags

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import br.pucminas.teamworktask.databinding.LayoutTagColorAdapterBinding
import br.pucminas.teamworktask.models.CorEnum

class TagCorAdapter(private val mContext: Context,
                    private val mLayoutResourceId: Int, private val corEnumList: List<CorEnum>): ArrayAdapter<CorEnum>(mContext, mLayoutResourceId, corEnumList){
    private lateinit var binding: LayoutTagColorAdapterBinding
    private var allCores: List<CorEnum> = ArrayList(corEnumList)

    override fun getCount(): Int {
        return corEnumList.size -1
    }
    override fun getItem(position: Int): CorEnum {
        return corEnumList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        binding = if (row == null) {
            val inflater = (mContext as Activity).layoutInflater
            LayoutTagColorAdapterBinding.inflate(inflater, parent, false)

        } else {
            LayoutTagColorAdapterBinding.bind(row)
        }
        val enum = getItem(position)

        binding.apply {
            tagCorNomeTv.text = enum.nomeCor + " - " + enum.hexaCor
            tagListaBgIv.setColorFilter(Color.parseColor(enum.hexaCor))
        }

        return binding.root!!
    }
}