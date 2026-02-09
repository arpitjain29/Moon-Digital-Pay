package com.moon_digital_pay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moon_digital_pay.databinding.MobilePlanTypeLayoutBinding

class MobileRechargePlanTypeAdapter(private val mList: List<String>, private val context: Context)
    : RecyclerView.Adapter<MobileRechargePlanTypeAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(MobilePlanTypeLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.layoutBinding.tvPlanTypes.text = mList[position]
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ItemViewHolder(val layoutBinding: MobilePlanTypeLayoutBinding)
        : RecyclerView.ViewHolder(layoutBinding.root)
}