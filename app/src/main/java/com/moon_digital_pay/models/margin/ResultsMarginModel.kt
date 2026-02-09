package com.moon_digital_pay.models.margin

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class ResultsMarginModel {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("operator_commission")
    var operatorCommission: List<OperatorCommissionMarginModel>? = null

    @SerializedName("package_name")
    var packageName: String? = null

    @SerializedName("role_type")
    var roleType: String? = null

    @SerializedName("user_id")
    var userId: String? = null
}
