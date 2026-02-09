package com.moon_digital_pay.models.rechargehistory

import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Suppress("unused")
class OperatorRechargeHistoryModel : Serializable {
    @SerializedName("chillarpay_operator_id")
    var chillarpayOperatorId: Any? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("distributor_rchg_commission")
    var distributorRchgCommission: String? = null

    @SerializedName("error")
    var error: Any? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("image_url")
    var imageUrl: String? = null

    @SerializedName("is_update")
    var isUpdate: String? = null

    @SerializedName("manual_recharge")
    var manualRecharge: String? = null

    @SerializedName("manual_recharge_amount")
    var manualRechargeAmount: Any? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("operator_code")
    var operatorCode: String? = null

    @SerializedName("operator_code_lapu")
    var operatorCodeLapu: String? = null

    @SerializedName("operator_id")
    var operatorId: String? = null

    @SerializedName("operator_name")
    var operatorName: String? = null

    @SerializedName("operator_type")
    var operatorType: String? = null

    @SerializedName("plans_api_operator_code")
    var plansApiOperatorCode: String? = null

    @SerializedName("recharge_plan_operator_code")
    var rechargePlanOperatorCode: String? = null

    @SerializedName("recharge_via")
    var rechargeVia: String? = null

    @SerializedName("retailer_rchg_commission")
    var retailerRchgCommission: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("super_distributor_rchg_commission")
    var superDistributorRchgCommission: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("updated_datetime")
    var updatedDatetime: String? = null
}
