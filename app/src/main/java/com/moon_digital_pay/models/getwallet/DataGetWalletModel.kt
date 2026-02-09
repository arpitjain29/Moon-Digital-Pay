package com.moon_digital_pay.models.getwallet

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class DataGetWalletModel {
    @SerializedName("transaction")
    var transaction: TransactionGetWalletModel? = null

    @SerializedName("wallet")
    var wallet: String? = null
}
