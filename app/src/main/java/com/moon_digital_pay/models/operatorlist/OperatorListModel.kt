package com.moon_digital_pay.models.operatorlist

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class OperatorListModel :CommonModel(){
    @SerializedName("data")
    var data: MutableList<DataOperatorListModel>? = null
}