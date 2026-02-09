package com.moon_digital_pay.models.login

import com.google.gson.annotations.SerializedName

class DataLoginModel {
    @SerializedName("user")
    var user: UserLoginModel? = UserLoginModel()
    @SerializedName("access_token")
    var accessToken: AccessTokenLoginModel? = AccessTokenLoginModel()
}