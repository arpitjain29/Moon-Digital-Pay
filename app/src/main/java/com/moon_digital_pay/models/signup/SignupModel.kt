package com.moon_digital_pay.models.signup

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class SignupModel :CommonModel(){
    @SerializedName("data")
    var data: DataSignupModel? = DataSignupModel()
}