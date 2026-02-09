package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class CheckOperatorParams {
    @SerializedName("number")
    var number: String? = null
    @SerializedName("operator_type")
    var operatorType: String? = null
}