package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class RechargeHistoryParams {
    @SerializedName("end_date")
    var endDate: String? = null

    @SerializedName("operator_id")
    var operatorId: String? = null

    @SerializedName("start_date")
    var startDate: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("user_id")
    var userId: String? = null
}
