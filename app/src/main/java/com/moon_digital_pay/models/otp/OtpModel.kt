package com.moon_digital_pay.models.otp

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class OtpModel :CommonModel(){
    @SerializedName("data")
    var data: DataOtpModel? = DataOtpModel()
}