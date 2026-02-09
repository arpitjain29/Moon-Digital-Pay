package com.moon_digital_pay.models.rechargehistory

import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Suppress("unused")
class RechargeRechargeHistoryModel : Serializable {
    @SerializedName("amount")
    var amount: String? = null

    @SerializedName("closing_balence")
    var closingBalence: String? = null

    @SerializedName("commission")
    var commission: String? = null

    @SerializedName("commission_amount")
    var commissionAmount: String? = null

    @SerializedName("commission_id")
    var commissionId: String? = null

    @SerializedName("commission_type")
    var commissionType: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("notification_status")
    var notificationStatus: String? = null

    @SerializedName("openning_balence")
    var openningBalence: String? = null

    @SerializedName("operator")
    var operator: OperatorRechargeHistoryModel? = null

    @SerializedName("operator_id")
    var operatorId: String? = null

    @SerializedName("recharge_number")
    var rechargeNumber: String? = null

    @SerializedName("recharge_via")
    var rechargeVia: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("wt_id")
    var wtId: String? = null
}
