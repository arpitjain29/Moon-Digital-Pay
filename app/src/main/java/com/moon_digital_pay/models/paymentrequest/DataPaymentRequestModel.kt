package com.moon_digital_pay.models.paymentrequest

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class DataPaymentRequestModel {
    @SerializedName("amount")
    var amount: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("image_url")
    var imageUrl: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("utr_number")
    var utrNumber: String? = null
}
