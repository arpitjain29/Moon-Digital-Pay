package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class ChangePasswordParams {
    @SerializedName("current_password")
    var currentPassword: String? = null
    @SerializedName("new_password")
    var newPassword: String? = null
    @SerializedName("confirm_password")
    var confirmPassword: String? = null
}