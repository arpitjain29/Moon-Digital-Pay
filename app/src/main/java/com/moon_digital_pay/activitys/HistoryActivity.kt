package com.moon_digital_pay.activitys

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityHistoryBinding
import com.moon_digital_pay.models.getwallet.DatumGetWalletModel
import com.moon_digital_pay.models.rechargehistory.DatumRechargeHistoryModel
import com.moon_digital_pay.utils.BaseActivity

class HistoryActivity : BaseActivity() {

    private var historyActivity: ActivityHistoryBinding? = null
    private var datumRechargeHistoryModel: DatumRechargeHistoryModel? = null
    private var datumGetWalletModel: DatumGetWalletModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyActivity = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(historyActivity!!.root)

        historyActivity!!.ivBackOrder.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (intent.getStringExtra("screenIntent").equals("recharge")) {
            datumRechargeHistoryModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    "listOrderHistory",
                    DatumRechargeHistoryModel::class.java
                )
            } else {
                intent.getSerializableExtra("listOrderHistory") as DatumRechargeHistoryModel
            }

            historyActivity!!.tvTransactionIdOrderHistory.text =
                datumRechargeHistoryModel?.transactionId
            historyActivity!!.tvDateAndTimeOrderHistory.text = datumRechargeHistoryModel?.createdAt
            Glide.with(mContext!!).load(datumRechargeHistoryModel?.recharge?.operator?.imageUrl)
                .error(R.drawable.ic_app_logo).placeholder(R.drawable.ic_app_logo)
                .into(historyActivity!!.ivImageOrderHistory)

            historyActivity!!.tvMobileOrderHistory.text =
                datumRechargeHistoryModel?.recharge?.rechargeNumber

            historyActivity!!.tvStatusOrderHistory.text =
                datumRechargeHistoryModel?.recharge?.status

            if (datumRechargeHistoryModel?.recharge?.status.equals(Constants.success)) {
                historyActivity!!.tvStatusOrderHistory.setTextColor(
                    ContextCompat.getColor(mContext!!, R.color.Color_008000)
                )
            } else {
                historyActivity!!.tvStatusOrderHistory.setTextColor(
                    ContextCompat.getColor(mContext!!, R.color.errorColor)
                )
            }

            historyActivity!!.tvAmountOrderHistory.text =
                String.format(
                    "%s %s", getString(R.string.rs),
                    datumRechargeHistoryModel?.recharge?.amount
                )
            historyActivity!!.tvDescriptionOrderHistory.text =
                datumRechargeHistoryModel?.description

        } else {
            datumGetWalletModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("listWalletHistory", DatumGetWalletModel::class.java)
            } else {
                intent.getSerializableExtra("listWalletHistory") as DatumGetWalletModel
            }

            historyActivity!!.tvTransactionIdOrderHistory.text =
                datumGetWalletModel?.transactionId
            historyActivity!!.tvDateAndTimeOrderHistory.text = datumGetWalletModel?.createdAt

            if (datumGetWalletModel?.rechargeNumber == null) {
                historyActivity!!.tvMobileOrderHistory.text = "N/A"
            } else {
                historyActivity!!.tvMobileOrderHistory.text = datumGetWalletModel?.rechargeNumber
            }

            historyActivity!!.tvStatusOrderHistory.text = datumGetWalletModel?.transactionStatus

            if (datumGetWalletModel?.transactionStatus.equals(Constants.success)) {
                historyActivity!!.tvStatusOrderHistory.setTextColor(
                    ContextCompat.getColor(mContext!!, R.color.Color_008000)
                )
            } else {
                historyActivity!!.tvStatusOrderHistory.setTextColor(
                    ContextCompat.getColor(mContext!!, R.color.errorColor)
                )
            }

            historyActivity!!.tvAmountOrderHistory.text =
                String.format(
                    "%s %s", getString(R.string.rs),
                    datumGetWalletModel?.amount
                )
            historyActivity!!.tvDescriptionOrderHistory.text = datumGetWalletModel?.description
        }
    }
}