package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class UpdateProfileParams {
    @SerializedName("address")
    var address: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("name")
    var name: String? = null
}
