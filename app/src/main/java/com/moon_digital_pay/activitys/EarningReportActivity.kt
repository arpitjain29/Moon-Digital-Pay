package com.moon_digital_pay.activitys

import android.os.Bundle
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityEarningReportBinding
import com.moon_digital_pay.models.earningreport.EarningReportModel
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class EarningReportActivity : BaseActivity() {

    private var earningReportBinding: ActivityEarningReportBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        earningReportBinding = ActivityEarningReportBinding.inflate(layoutInflater)
        setContentView(earningReportBinding!!.root)

        earningReportApi()
    }

    private fun earningReportApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).earningReport()
            call1?.enqueue(object : Callback<EarningReportModel?> {
                override fun onResponse(
                    call: Call<EarningReportModel?>, response: Response<EarningReportModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val earningReportModel: EarningReportModel? = response.body()
                        if (earningReportModel != null) {
                            earningReportBinding!!.tvEarningAmount.text =
                                String.format(
                                    "%s %s", getString(R.string.rs),
                                    earningReportModel.data?.todayEarning
                                )
                            earningReportBinding!!.tvTotalRecharge.text = String.format(
                                "%s %s",
                                getString(R.string.rs),
                                earningReportModel.data?.todayRecharge
                            )
                            earningReportBinding!!.tvTotalEarning.text = String.format(
                                "%s %s",
                                getString(R.string.rs),
                                earningReportModel.data?.totalEarning
                            )
                            earningReportBinding!!.tvDaysEarning.text = String.format(
                                "%s %s",
                                getString(R.string.rs),
                                earningReportModel.data?.last7Days
                            )
                            earningReportBinding!!.tvDaysEarning30.text = String.format(
                                "%s %s",
                                getString(R.string.rs),
                                earningReportModel.data?.last30Days
                            )
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            try {
                                val errorJson = JSONObject(errorBody)
                                val errorArray = errorJson.getJSONArray("error")
                                val errorMessage = errorArray.getJSONObject(0).getString("message")
                                CommonFunction.showToastSingle(mContext, errorMessage, 0)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                CommonFunction.showToastSingle(
                                    mContext, "An error occurred. Please try again.", 0
                                )
                            }
                        }
                    }
                }

                override fun onFailure(p0: Call<EarningReportModel?>, throwable: Throwable) {
                    hideProgressDialog()
                    throwable.printStackTrace()
                    if (throwable is HttpException) {
                        throwable.printStackTrace()
                    }
                }

            })
        } else {
            CommonFunction.showToastSingle(
                mContext, resources.getString(R.string.net_connection), 0
            )
        }
    }
}