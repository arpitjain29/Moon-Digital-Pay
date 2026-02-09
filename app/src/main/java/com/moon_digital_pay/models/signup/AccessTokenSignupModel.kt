package com.moon_digital_pay.models.signup

import com.google.gson.annotations.SerializedName

class AccessTokenSignupModel {
    @SerializedName("token")
    var token: String? = null
    @SerializedName("type")
    var type: String? = null
}