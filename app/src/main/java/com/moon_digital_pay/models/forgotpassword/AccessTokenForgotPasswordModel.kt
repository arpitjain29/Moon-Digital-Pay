package com.moon_digital_pay.models.forgotpassword

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class AccessTokenForgotPasswordModel {
    @SerializedName("token")
    var token: String? = null

    @SerializedName("type")
    var type: String? = null
}
