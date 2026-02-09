package com.moon_digital_pay.models.login

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class LoginModel :CommonModel(){
    @SerializedName("data")
    var data: DataLoginModel? = DataLoginModel()
}