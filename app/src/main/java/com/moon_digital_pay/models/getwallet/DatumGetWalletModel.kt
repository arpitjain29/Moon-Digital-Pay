package com.moon_digital_pay.models.getwallet

import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Suppress("unused")
class DatumGetWalletModel :Serializable{
    @SerializedName("amount")
    var amount: String? = null

    @SerializedName("closing_balence")
    var closingBalence: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("openning_balence")
    var openningBalence: String? = null

    @SerializedName("recharge_id")
    var rechargeId: String? = null

    @SerializedName("recharge_number")
    var rechargeNumber: String? = null

    @SerializedName("trans_reference_id")
    var transReferenceId: Any? = null

    @SerializedName("transaction_id")
    var transactionId: String? = null

    @SerializedName("transaction_status")
    var transactionStatus: String? = null

    @SerializedName("transaction_type")
    var transactionType: String? = null

    @SerializedName("transaction_userid")
    var transactionUserid: Any? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("wt_type")
    var wtType: String? = null
}
