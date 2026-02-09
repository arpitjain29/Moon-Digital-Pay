package com.moon_digital_pay.models.checkrechargeplan

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class CheckRechargePlanModel : CommonModel() {
    @SerializedName("results")
    var results: List<ResultsCheckRechargePlanModel>? = null
}