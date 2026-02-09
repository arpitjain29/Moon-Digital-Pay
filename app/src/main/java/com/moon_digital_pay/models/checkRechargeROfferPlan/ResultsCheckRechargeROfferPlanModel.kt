package com.moon_digital_pay.models.checkRechargeROfferPlan

import com.google.gson.annotations.SerializedName

class ResultsCheckRechargeROfferPlanModel {
    @SerializedName("price")
    var price: String? = null
    @SerializedName("commissionUnit")
    var commissionUnit: String? = null
    @SerializedName("ofrtext")
    var ofrtext: String? = null
    @SerializedName("logdesc")
    var logdesc: String? = null
    @SerializedName("commissionAmount")
    var commissionAmount: String? = null
    @SerializedName("validity")
    var validity: String? = null
}