package com.moon_digital_pay.models.margin

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class MarginModel :CommonModel(){
    @SerializedName("results")
    var results: ResultsMarginModel? = null
}
