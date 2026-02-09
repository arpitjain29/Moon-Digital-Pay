package com.moon_digital_pay.models.signup

import com.google.gson.annotations.SerializedName

class UserSignupModel {
    @SerializedName("first_name")
    var firstName: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("latitude")
    var latitude: String? = null
    @SerializedName("longitude")
    var longitude: String? = null
    @SerializedName("pincode")
    var pincode: String? = null
    @SerializedName("role")
    var role: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("full_name")
    var fullName: String? = null
    @SerializedName("image_url")
    var imageUrl: String? = null
}