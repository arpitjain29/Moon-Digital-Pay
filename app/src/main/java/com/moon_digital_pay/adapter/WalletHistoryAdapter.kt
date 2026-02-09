package com.moon_digital_pay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moon_digital_pay.R
import com.moon_digital_pay.databinding.WalletHistoryLayoutBinding
import com.moon_digital_pay.models.getwallet.DatumGetWalletModel
import com.moon_digital_pay.utils.interfacef.WalletHistoryInterface

class WalletHistoryAdapter(
    private val datumGetWalletList: List<DatumGetWalletModel>,
    private val context: Context,
    private val walletHistoryInterface: WalletHistoryInterface,
) : RecyclerView.Adapter<WalletHistoryAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            WalletHistoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.layoutBinding.tvDetailsWalletLayout.text = datumGetWalletList[position].description
        holder.layoutBinding.tvDateTimeWalletLayout.text = datumGetWalletList[position].createdAt
        holder.layoutBinding.tvOpenBalanceWalletLayout.text =
            String.format(
                "%s %s", context.resources.getString(R.string.opening_balance),
                datumGetWalletList[position].openningBalence
            )
        holder.layoutBinding.tvClosingBalanceWalletLayout.text =
            String.format(
                "%s %s", context.resources.getString(R.string.closing_balance),
                datumGetWalletList[position].closingBalence
            )
        holder.layoutBinding.tvStatusWalletLayout.text = datumGetWalletList[position].transactionStatus
        holder.layoutBinding.tvStatusRechargeWalletLayout.text =
            datumGetWalletList[position].wtType
        holder.layoutBinding.tvRechargeAmountWalletLayout.text =
            String.format(
                "%s %s", context.resources.getString(R.string.rs),
                datumGetWalletList[position].amount
            )

        holder.layoutBinding.llWalletHistory.setOnClickListener {
            walletHistoryInterface.walletHistoryClick(datumGetWalletList[position],position)
        }
    }

    override fun getItemCount(): Int {
        return datumGetWalletList.size
    }

    class ItemViewHolder(val layoutBinding: WalletHistoryLayoutBinding) :
        RecyclerView.ViewHolder(layoutBinding.root)
}