package com.moon_digital_pay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moon_digital_pay.R
import com.moon_digital_pay.databinding.MarginLayoutBinding
import com.moon_digital_pay.models.margin.OperatorCommissionMarginModel

class MarginsAdapter(private val operatorCommissionMarginModelList: List<OperatorCommissionMarginModel>,
                     private val context: Context)
    : RecyclerView.Adapter<MarginsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(MarginLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.layoutBinding.tvOperatorAmount.text = operatorCommissionMarginModelList[position].commission
        if (operatorCommissionMarginModelList[position].operator != null){
            holder.layoutBinding.tvOperatorNameMargin.text = operatorCommissionMarginModelList[position].operator?.name
            Glide.with(context)
                .load(operatorCommissionMarginModelList[position].operator?.imageUrl)
                .placeholder(R.drawable.ic_placeholder_image).error(R.drawable.ic_placeholder_image)
                .into(holder.layoutBinding.ivOperatorImageMargin)
        }

    }

    override fun getItemCount(): Int {
        return operatorCommissionMarginModelList.size
    }

    class ItemViewHolder(val layoutBinding: MarginLayoutBinding)
        : RecyclerView.ViewHolder(layoutBinding.root)
}