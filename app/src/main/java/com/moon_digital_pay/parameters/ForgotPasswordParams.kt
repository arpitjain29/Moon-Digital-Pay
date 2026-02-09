package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class ForgotPasswordParams {
    @SerializedName("phone_number")
    var phoneNumber: String? = null
}