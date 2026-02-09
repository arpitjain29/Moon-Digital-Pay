package com.moon_digital_pay.parameters

import com.google.gson.annotations.SerializedName

class SignupParams {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("latitude")
    var latitude: String? = null
    @SerializedName("longitude")
    var longitude: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("pincode")
    var pincode: String? = null
    @SerializedName("device_id")
    var deviceId: String? = null
    @SerializedName("device_token")
    var deviceToken: String? = null
    @SerializedName("device_type")
    var deviceType: String? = null
}