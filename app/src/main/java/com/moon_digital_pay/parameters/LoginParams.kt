package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class LoginParams {
    @SerializedName("phone_number")
    var phoneNumber: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("device_token")
    var deviceToken: String? = null

    @SerializedName("device_type")
    var deviceType: String? = null
}