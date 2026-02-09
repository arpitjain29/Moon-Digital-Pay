package com.moon_digital_pay.models.dashboard

import com.google.gson.annotations.SerializedName

class DataDashboardModel {
    @SerializedName("banners")
    var banners: MutableList<BannersDashboardModel>? = null
    @SerializedName("walle")
    var walle: String? = null
    @SerializedName("user_info")
    var userInfo: UserInfoDashboardModel? = UserInfoDashboardModel()
}