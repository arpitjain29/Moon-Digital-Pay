package com.moon_digital_pay.models.dashboard

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class DashboardModel :CommonModel(){
    @SerializedName("data")
    var data: DataDashboardModel? = DataDashboardModel()
}