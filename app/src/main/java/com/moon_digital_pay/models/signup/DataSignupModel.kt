package com.moon_digital_pay.models.signup

import com.google.gson.annotations.SerializedName

class DataSignupModel {
    @SerializedName("user")
    var user: UserSignupModel? = UserSignupModel()
    @SerializedName("access_token")
    var accessToken: AccessTokenSignupModel? = AccessTokenSignupModel()
}