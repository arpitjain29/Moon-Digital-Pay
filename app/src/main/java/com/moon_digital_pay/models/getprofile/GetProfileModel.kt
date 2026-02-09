package com.moon_digital_pay.models.getprofile

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class GetProfileModel :CommonModel(){
    @SerializedName("data")
    var data: ArrayList<DataGetProfileModel> = arrayListOf()
}