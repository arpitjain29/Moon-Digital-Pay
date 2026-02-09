package com.moon_digital_pay.models.checkoperator

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel
import com.moon_digital_pay.models.operatorlist.DataOperatorListModel
import com.moon_digital_pay.models.regions.RegionsItemModel

class CheckOperatorModel :CommonModel(){
    @SerializedName("results")
    var results: DataOperatorListModel? = null
    @SerializedName("circle")
    var circle: RegionsItemModel? = null
}