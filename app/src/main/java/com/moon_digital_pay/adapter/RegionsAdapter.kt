package com.moon_digital_pay.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.moon_digital_pay.R
import com.moon_digital_pay.databinding.HomeLayoutBinding
import com.moon_digital_pay.databinding.RegionsItemLayoutBinding
import com.moon_digital_pay.models.regions.RegionsItemModel
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.HomeClickInterface

class RegionsAdapter(
    private val mList: List<RegionsItemModel>,
    private val context: Activity,
    private val homeClickInterface: HomeClickInterface
) : RecyclerView.Adapter<RegionsAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RegionsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {


        holder.layoutBinding.tvRegionsItem.text = mList[position].stateName

          holder.layoutBinding.tvRegionsItem.setOnClickListener {
            homeClickInterface.homeClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ItemViewHolder(val layoutBinding: RegionsItemLayoutBinding) :
        RecyclerView.ViewHolder(layoutBinding.root)
}