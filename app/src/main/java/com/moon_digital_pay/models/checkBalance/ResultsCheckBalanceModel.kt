package com.moon_digital_pay.models.checkBalance

import com.google.gson.annotations.SerializedName

class ResultsCheckBalanceModel {
    @SerializedName("wallet")
    var wallet: String? = null
    @SerializedName("notification")
    var notification: String? = null
    @SerializedName("payment_gateway_type")
    var paymentGatewayType: String? = null
    @SerializedName("forceful_kyc_update")
    var forcefulKycUpdate: String? = null
    @SerializedName("aadhar_verify_status")
    var aadharVerifyStatus: String? = null
}