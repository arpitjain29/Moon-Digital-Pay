package com.moon_digital_pay.models.forgotpassword

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.login.AccessTokenLoginModel

@Suppress("unused")
class DataForgotPasswordModel {
    @SerializedName("access_token")
    var accessToken: AccessTokenLoginModel? = null
}
