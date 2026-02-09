package com.moon_digital_pay.models.otp

import com.google.gson.annotations.SerializedName

class AccessTokenOtpModel {
    @SerializedName("token")
    var token: String? = null
    @SerializedName("type")
    var type: String? = null
}