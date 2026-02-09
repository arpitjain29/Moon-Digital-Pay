package com.moon_digital_pay.models.earningreport

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class DataEarningReportModel {
    @SerializedName("last_30_days")
    var last30Days: String? = null

    @SerializedName("last_7_days")
    var last7Days: String? = null

    @SerializedName("today_earning")
    var todayEarning: String? = null

    @SerializedName("today_recharge")
    var todayRecharge: String? = null

    @SerializedName("total_earning")
    var totalEarning: String? = null
}
