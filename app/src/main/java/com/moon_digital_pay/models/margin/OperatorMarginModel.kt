package com.moon_digital_pay.models.margin

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class OperatorMarginModel {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("image_url")
    var imageUrl: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("operator_id")
    var operatorId: String? = null
}
