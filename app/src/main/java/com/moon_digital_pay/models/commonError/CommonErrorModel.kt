package com.moon_digital_pay.models.commonError

import com.google.gson.annotations.SerializedName

class CommonErrorModel {
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("error")
    var error: ArrayList<ErrorCommonErrorModel> = arrayListOf()
    @SerializedName("data")
    var data: ArrayList<String> = arrayListOf()
    @SerializedName("message")
    var message: String? = null
}