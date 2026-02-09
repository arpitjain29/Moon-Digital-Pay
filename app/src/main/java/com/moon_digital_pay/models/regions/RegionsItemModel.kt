package com.moon_digital_pay.models.regions

import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Suppress("unused")
class RegionsItemModel: Serializable {
    @SerializedName("circle_code")
    var circleCode: String? = null

    @SerializedName("country_id")
    var countryId: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("operator_circle_code")
    var operatorCircleCode: String? = null

    @SerializedName("state_name")
    var stateName: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null
}
