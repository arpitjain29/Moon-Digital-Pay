package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class CheckRechargeROfferPlanParams {
    @SerializedName("operator_id")
    var operatorId: Int? = null
    @SerializedName("mobile_no")
    var mobileNo: String? = null
}