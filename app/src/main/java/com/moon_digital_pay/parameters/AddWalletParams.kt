package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.apiUtils.ApiUrlEndpoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddWalletParams {
    @SerializedName("amount")
    var amount: String? = null
    @SerializedName("utr_number")
    var utrNumber: String? = null

    fun toUpdateProfileMap(): MutableMap<String, RequestBody> {
        val requestBody: MutableMap<String, RequestBody> = HashMap()
        amount?.toRequestBody(
            MultipartBody.FORM
        )?.let {
            requestBody.put(
                ApiUrlEndpoint.amount, it
            )
        }
        utrNumber?.toRequestBody(
            MultipartBody.FORM
        )?.let {
            requestBody.put(
                ApiUrlEndpoint.utr_number, it
            )
        }
        println(requestBody)
        return requestBody
    }
}