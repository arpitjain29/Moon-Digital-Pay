package com.moon_digital_pay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moon_digital_pay.databinding.MobileRechargeLayoutBinding
import com.moon_digital_pay.models.checkRechargeROfferPlan.ResultsCheckRechargeROfferPlanModel
import com.moon_digital_pay.models.checkrechargeplan.PlansCheckRechargePlanModel

class MobileRechargeAdapter(private val resultsCheckRechargeROfferPlanModel: List<ResultsCheckRechargeROfferPlanModel>,
                            private val context: Context)
    : RecyclerView.Adapter<MobileRechargeAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(MobileRechargeLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.layoutBinding.tvRechargeDetails.text = resultsCheckRechargeROfferPlanModel[position].ofrtext
        holder.layoutBinding.tvRechargeAmount.text = resultsCheckRechargeROfferPlanModel[position].price.toString()
        holder.layoutBinding.tvRechargeDays.text = resultsCheckRechargeROfferPlanModel[position].logdesc
    }

    override fun getItemCount(): Int {
        return resultsCheckRechargeROfferPlanModel.size
    }

    class ItemViewHolder(val layoutBinding: MobileRechargeLayoutBinding)
        : RecyclerView.ViewHolder(layoutBinding.root)
}