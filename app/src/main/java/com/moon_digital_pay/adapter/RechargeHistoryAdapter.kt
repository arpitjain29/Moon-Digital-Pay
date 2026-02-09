package com.moon_digital_pay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.RechargeHistoryLayoutBinding
import com.moon_digital_pay.models.rechargehistory.DatumRechargeHistoryModel
import com.moon_digital_pay.utils.interfacef.OrderHistoryInterface

class RechargeHistoryAdapter(
    private val datumRechargeHistoryModelList: MutableList<DatumRechargeHistoryModel>,
    private val context: Context,
    private val orderHistoryInterface: OrderHistoryInterface,
) : RecyclerView.Adapter<RechargeHistoryAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RechargeHistoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.layoutBinding.tvMobileNumberRecharge.text =
            datumRechargeHistoryModelList[position].recharge?.rechargeNumber
        holder.layoutBinding.tvMobileNumberAmount.text =
            String.format(
                "%s %s", context.resources.getString(R.string.rs),
                datumRechargeHistoryModelList[position].recharge?.amount
            )
        holder.layoutBinding.tvMobileNumberDetails.text =
            datumRechargeHistoryModelList[position].description
        holder.layoutBinding.tvRechargeHistoryTxnNo.text =
            String.format(
                "%s %s", context.resources.getString(R.string.txn_no),
                datumRechargeHistoryModelList[position].transactionId
            )
        holder.layoutBinding.tvDateTimeRechargeHistory.text =
            datumRechargeHistoryModelList[position].createdAt
        holder.layoutBinding.tvRechargeStatus.text =
            datumRechargeHistoryModelList[position].recharge?.status
        holder.layoutBinding.tvOpenBalanceHistory.text =
            String.format(
                "%s %s", context.resources.getString(R.string.opening_balance),
                datumRechargeHistoryModelList[position].openningBalence
            )
        holder.layoutBinding.tvClosingBalanceHistory.text =
            String.format(
                "%s %s", context.resources.getString(R.string.closing_balance),
                datumRechargeHistoryModelList[position].closingBalence
            )

        if (datumRechargeHistoryModelList[position].recharge?.status.equals(Constants.success)) {
            holder.layoutBinding.tvRechargeStatus.setTextColor(
                ContextCompat.getColor(context, R.color.Color_008000)
            )
        } else {
            holder.layoutBinding.tvRechargeStatus.setTextColor(
                ContextCompat.getColor(context, R.color.errorColor)
            )
        }

        Glide.with(context)
            .load(datumRechargeHistoryModelList[position].recharge?.operator?.imageUrl)
            .placeholder(R.drawable.ic_jio_logo).error(R.drawable.ic_jio_logo)
            .into(holder.layoutBinding.ivRechargeMobileOperator)

        holder.layoutBinding.llRechargeHistoryClick.setOnClickListener {
            orderHistoryInterface.orderHistoryClick(datumRechargeHistoryModelList[position],position)
        }
    }

    override fun getItemCount(): Int {
        return datumRechargeHistoryModelList.size
    }

    class ItemViewHolder(val layoutBinding: RechargeHistoryLayoutBinding) :
        RecyclerView.ViewHolder(layoutBinding.root)
}