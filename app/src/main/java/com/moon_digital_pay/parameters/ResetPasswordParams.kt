package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class ResetPasswordParams {
    @SerializedName("new_password")
    var newPassword: String? = null

    @SerializedName("otp_code")
    var otpCode: String? = null
}
