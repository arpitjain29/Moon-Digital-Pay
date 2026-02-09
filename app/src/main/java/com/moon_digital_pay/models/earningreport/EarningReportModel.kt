package com.moon_digital_pay.models.earningreport

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class EarningReportModel :CommonModel(){
    @SerializedName("data")
    var data: DataEarningReportModel? = null
}
