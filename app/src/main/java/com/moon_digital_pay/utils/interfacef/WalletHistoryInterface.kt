package com.moon_digital_pay.utils.interfacef

import com.moon_digital_pay.models.getwallet.DatumGetWalletModel

interface WalletHistoryInterface {
    fun walletHistoryClick(data: DatumGetWalletModel, position: Int?)
}