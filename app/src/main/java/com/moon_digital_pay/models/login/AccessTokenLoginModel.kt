package com.moon_digital_pay.models.login

import com.google.gson.annotations.SerializedName

class AccessTokenLoginModel {
    @SerializedName("token")
    var token: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("expires_in")
    var expiresIn: Int? = null
}