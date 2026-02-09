package com.moon_digital_pay.models.updateprofile

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class UpdateProfileModel :CommonModel(){
    @SerializedName("data")
    var data: DataUpdateProfileModel? = null
}