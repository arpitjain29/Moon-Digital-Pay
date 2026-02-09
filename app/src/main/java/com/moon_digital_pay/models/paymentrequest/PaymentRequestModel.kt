package com.moon_digital_pay.models.paymentrequest

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class PaymentRequestModel :CommonModel(){
    @SerializedName("data")
    var data: DataPaymentRequestModel? = null

    @SerializedName("success")
    var success: Boolean? = null
}
