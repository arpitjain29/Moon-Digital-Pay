package com.moon_digital_pay.models.forgotpassword

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class ForgotPasswordModel : CommonModel() {
    @SerializedName("data")
    var data: DataForgotPasswordModel? = null

}
