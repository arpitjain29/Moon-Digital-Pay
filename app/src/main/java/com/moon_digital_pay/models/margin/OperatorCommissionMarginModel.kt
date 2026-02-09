package com.moon_digital_pay.models.margin

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class OperatorCommissionMarginModel {
    @SerializedName("commission")
    var commission: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("operator")
    var operator: OperatorMarginModel? = null

    @SerializedName("operator_id")
    var operatorId: String? = null

    @SerializedName("package_id")
    var packageId: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null
}
