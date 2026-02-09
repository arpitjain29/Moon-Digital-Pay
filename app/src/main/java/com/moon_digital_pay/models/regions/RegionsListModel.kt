package com.moon_digital_pay.models.regions

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class RegionsListModel : CommonModel() {
    @SerializedName("data")
    var results: List<RegionsItemModel>? = null
}