package com.moon_digital_pay.models.referearn

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class ResultsReferEarnModel {
    @SerializedName("minimum_wallet_load_amount")
    var minimumWalletLoadAmount: String? = null

    @SerializedName("refer_code")
    var referCode: String? = null

    @SerializedName("referral_amounts")
    var referralAmounts: String? = null

    @SerializedName("referral_earnings")
    var referralEarnings: String? = null
}
