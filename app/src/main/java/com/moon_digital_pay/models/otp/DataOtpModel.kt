package com.moon_digital_pay.models.otp

import com.google.gson.annotations.SerializedName

class DataOtpModel {
    @SerializedName("access_token")
    var accessToken: AccessTokenOtpModel? = AccessTokenOtpModel()
}