package com.moon_digital_pay.models.checkRechargeROfferPlan

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class CheckRechargeROfferPlanModel : CommonModel(){
    @SerializedName("results")
    var results: ArrayList<ResultsCheckRechargeROfferPlanModel> = arrayListOf()
}