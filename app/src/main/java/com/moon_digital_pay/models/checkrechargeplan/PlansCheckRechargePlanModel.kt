package com.moon_digital_pay.models.checkrechargeplan

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlansCheckRechargePlanModel : Serializable{
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("amount")
    var amount: String? = null
    @SerializedName("validity")
    var validity: String? = null
    @SerializedName("talktime")
    var talktime: String? = null
    @SerializedName("validityDays")
    var validityDays: Int? = null
    @SerializedName("benefit")
    var benefit: String? = null
    @SerializedName("calls")
    var calls: String? = null
    @SerializedName("sms")
    var sms: String? = null
    @SerializedName("data")
    var data: String? = null
    @SerializedName("subscriptions")
    var subscriptions: ArrayList<SubscriptionsCheckRechargePlanModel> = arrayListOf()
    @SerializedName("remark")
    var remark: String? = null
    @SerializedName("rechargeable")
    var rechargeable: Boolean? = null
    @SerializedName("dailyCost")
    var dailyCost: Double? = null
    @SerializedName("addedAt")
    var addedAt: String? = null
}