package com.moon_digital_pay.models.checkrechargeplan

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResultsCheckRechargePlanModel : Serializable {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("fullName")
    var fullName: String? = null
    @SerializedName("plans")
    var plans: ArrayList<PlansCheckRechargePlanModel>? = null
}