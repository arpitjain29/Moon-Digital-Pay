package com.moon_digital_pay.models.checkoperator

import com.google.gson.annotations.SerializedName

class CircleCheckOperatorModel {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("state_name")
    var stateName: String? = null
    @SerializedName("circle_code")
    var circleCode: String? = null
    @SerializedName("operator_circle_code")
    var operatorCircleCode: String? = null
}