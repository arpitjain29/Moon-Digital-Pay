package com.moon_digital_pay.models.rechargehistory

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class RechargeHistoryModel :CommonModel(){
    @SerializedName("results")
    var results: ResultsRechargeHistoryModel? = null
}
