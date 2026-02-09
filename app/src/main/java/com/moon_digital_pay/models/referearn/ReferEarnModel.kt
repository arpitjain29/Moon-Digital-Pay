package com.moon_digital_pay.models.referearn

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class ReferEarnModel :CommonModel(){
    @SerializedName("results")
    var results: ResultsReferEarnModel? = null
}
