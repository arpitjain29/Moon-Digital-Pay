package com.moon_digital_pay.models.getwallet

import com.google.gson.annotations.SerializedName
import com.moon_digital_pay.models.CommonModel

@Suppress("unused")
class GetWalletModel : CommonModel() {
    @SerializedName("data")
    var data: DataGetWalletModel? = null
}
