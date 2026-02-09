package com.moon_digital_pay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moon_digital_pay.R
import com.moon_digital_pay.databinding.PlanDetailsLayoutBinding
import com.moon_digital_pay.models.checkrechargeplan.PlansCheckRechargePlanModel
import com.moon_digital_pay.utils.interfacef.HomeClickInterface

class PlanDetailsAdapter(
    private val plansCheckRechargePlanModel: List<PlansCheckRechargePlanModel>,
    private val context: Context,
    private val homeClickInterface: HomeClickInterface
) : RecyclerView.Adapter<PlanDetailsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            PlanDetailsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.layoutBinding.tvRechargeAmountPlan.text = String.format(
            "%s %s",
            context.resources.getString(R.string.rs),
            plansCheckRechargePlanModel[position].amount.toString()
        )
        if (plansCheckRechargePlanModel[position].data.equals("")){
            holder.layoutBinding.tvDataPlan.text = "N/A"
        }else{
            holder.layoutBinding.tvDataPlan.text = plansCheckRechargePlanModel[position].data
        }
        holder.layoutBinding.tvPlanDays.text = plansCheckRechargePlanModel[position].validity
        holder.layoutBinding.tvPlanDetails.text = plansCheckRechargePlanModel[position].benefit
        holder.layoutBinding.llMainLayout.setOnClickListener {
            homeClickInterface.homeClick(position)
        }
    }

    override fun getItemCount(): Int {
        return plansCheckRechargePlanModel.size
    }

    class ItemViewHolder(val layoutBinding: PlanDetailsLayoutBinding) :
        RecyclerView.ViewHolder(layoutBinding.root)
}