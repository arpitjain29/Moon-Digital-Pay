package com.moon_digital_pay.models.dashboard

import com.google.gson.annotations.SerializedName

class BannersDashboardModel {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("image")
    var image: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    @SerializedName("image_url")
    var imageUrl: String? = null
}