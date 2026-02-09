package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class MobileRechargeParams {
    @SerializedName("operator_id")
    var operatorId: String? = null
    @SerializedName("phone_number")
    var phoneNumber: String? = null
    @SerializedName("amount")
    var amount: String? = null
}