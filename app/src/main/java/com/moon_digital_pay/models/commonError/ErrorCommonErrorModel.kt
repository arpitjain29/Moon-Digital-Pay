package com.moon_digital_pay.models.commonError

import com.google.gson.annotations.SerializedName

class ErrorCommonErrorModel {
    @SerializedName("field")
    var field: String? = null
    @SerializedName("message")
    var message: String? = null
}