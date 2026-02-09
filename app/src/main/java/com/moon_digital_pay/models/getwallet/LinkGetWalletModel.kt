package com.moon_digital_pay.models.getwallet

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class LinkGetWalletModel {
    @SerializedName("active")
    var active: Boolean? = null

    @SerializedName("label")
    var label: String? = null

    @SerializedName("url")
    var url: Any? = null
}
