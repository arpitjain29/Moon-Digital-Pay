package com.moon_digital_pay.models.rechargehistory

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class ResultsRechargeHistoryModel {
    @SerializedName("transaction")
    var transaction: TransactionRechargeHistoryModel? = null
}
