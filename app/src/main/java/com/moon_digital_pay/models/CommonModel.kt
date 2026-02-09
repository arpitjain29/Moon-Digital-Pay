package com.moon_digital_pay.models

import com.google.gson.annotations.SerializedName

open class CommonModel {
    @SerializedName("status")
    var status: Int? = null
    @SerializedName("message")
    var message: String? = null
}