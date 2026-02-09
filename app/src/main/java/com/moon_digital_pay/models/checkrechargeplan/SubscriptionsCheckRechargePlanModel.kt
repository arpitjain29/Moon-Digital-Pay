package com.moon_digital_pay.models.checkrechargeplan

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubscriptionsCheckRechargePlanModel : Serializable {
    @SerializedName("code")
    var code: String? = null
    @SerializedName("logo")
    var logo: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("popularity")
    var popularity: Int? = null
}