package br.pucminas.teamworktask.ui.privada.tags

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import br.pucminas.teamworktask.databinding.LayoutTagColorAdapterBinding
import br.pucminas.teamworktask.models.CorEnum

class TagCorAdapter(private val mContext: Context,
                    private val mLayoutResourceId: Int, private val corEnumList: List<CorEnum>): ArrayAdapter<CorEnum>(mContext, mLayoutResourceId, corEnumList){
    private lateinit var binding: LayoutTagColorAdapterBinding
    private val corEnumMutableList: MutableList<CorEnum> = ArrayList(corEnumList)
    private var allCores: List<CorEnum> = ArrayList(corEnumList)

    override fun getCount(): Int {
        return corEnumMutableList.size
    }
    override fun getItem(position: Int): CorEnum {
        return corEnumMutableList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            val inflater = (mContext as Activity).layoutInflater
            binding = LayoutTagColorAdapterBinding.inflate(inflater, parent, false)
        } else {
            binding = LayoutTagColorAdapterBinding.bind(row)
        }
        binding.apply {
            tagCorNomeTv.text = getItem(position).nomeCor + " - " + getItem(position).hexaCor
            tagCorNomeView.setColorFilter(Color.parseColor(getItem(position).hexaCor))
        }

        return row!!
    }
}