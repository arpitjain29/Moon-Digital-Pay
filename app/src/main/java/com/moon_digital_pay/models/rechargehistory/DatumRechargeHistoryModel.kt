package com.moon_digital_pay.models.rechargehistory

import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Suppress("unused")
class DatumRechargeHistoryModel : Serializable{
    @SerializedName("amount")
    var amount: String? = null

    @SerializedName("bank_name")
    var bankName: Any? = null

    @SerializedName("closing_balence")
    var closingBalence: String? = null

    @SerializedName("comments")
    var comments: Any? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("deleted_at")
    var deletedAt: Any? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("flat_amount")
    var flatAmount: Any? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("openning_balence")
    var openningBalence: String? = null

    @SerializedName("payment_mode")
    var paymentMode: Any? = null

    @SerializedName("payment_type")
    var paymentType: Any? = null

    @SerializedName("recharge")
    var recharge: RechargeRechargeHistoryModel? = null

    @SerializedName("recharge_id")
    var rechargeId: Any? = null

    @SerializedName("recharge_number")
    var rechargeNumber: String? = null

    @SerializedName("response_data")
    var responseData: String? = null

    @SerializedName("trans_reference_id")
    var transReferenceId: String? = null

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

    @SerializedName("user_upi_id")
    var userUpiId: Any? = null

    @SerializedName("wallet_add_distributor_commission")
    var walletAddDistributorCommission: String? = null

    @SerializedName("wallet_added_amount")
    var walletAddedAmount: String? = null

    @SerializedName("wallet_transfer_status")
    var walletTransferStatus: String? = null

    @SerializedName("wt_type")
    var wtType: String? = null
}
