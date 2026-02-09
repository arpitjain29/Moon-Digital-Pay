package com.moon_digital_pay.models.checkBalance

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

class CheckBalanceModel :CommonModel(){
    @SerializedName("results")
    var results: ResultsCheckBalanceModel? = ResultsCheckBalanceModel()
}