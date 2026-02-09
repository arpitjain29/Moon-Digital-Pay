package com.moon_digital_pay.models.operatorlist

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataOperatorListModel : Serializable{
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("operator_name")
    var operatorName: String? = null
    @SerializedName("image")
    var image: String? = null
    @SerializedName("operator_type")
    var operatorType: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("image_url")
    var imageUrl: String? = null
    @SerializedName("operator_id")
    var operatorId: String? = null
}