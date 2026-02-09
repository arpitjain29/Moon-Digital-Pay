package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class CheckRechargePlanParams {
    @SerializedName("operator_id")
    var operatorId: Int? = null
    @SerializedName("operator_circle")
    var operatorCircle: String? = null
    @SerializedName("operator_type")
    var operatorType: String? = null
}