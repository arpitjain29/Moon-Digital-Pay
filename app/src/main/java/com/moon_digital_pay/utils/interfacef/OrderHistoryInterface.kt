package com.moon_digital_pay.utils.interfacef

import com.moon_digital_pay.models.rechargehistory.DatumRechargeHistoryModel

interface OrderHistoryInterface {
    fun orderHistoryClick(data: DatumRechargeHistoryModel,position: Int?)
}