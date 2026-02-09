package com.moon_digital_pay.activitys

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.text.ClipboardManager
import android.widget.Toast
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityReferEarnBinding
import com.moon_digital_pay.models.referearn.ReferEarnModel
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ReferEarnActivity : BaseActivity() {

    private var referEarnActivity: ActivityReferEarnBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        referEarnActivity = ActivityReferEarnBinding.inflate(layoutInflater)
        setContentView(referEarnActivity!!.root)

        referEarnActivity!!.ivBackRefer.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        referEarnActivity!!.ivCopyText.setOnClickListener {
            mContext?.let { it1 ->
                setClipboard(
                    it1,
                    referEarnActivity!!.tvReferEarnCode.text.toString()
                )
            }
        }

        referEarnApi()
    }

    private fun setClipboard(context: Context, text: String) {
        if (SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
            Toast.makeText(mContext, "Copied", Toast.LENGTH_SHORT).show()
        } else {
            val clipboard =
                context.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(mContext, "Copied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun referEarnApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).getReferralApi()
            call1?.enqueue(object : Callback<ReferEarnModel?> {
                override fun onResponse(
                    call: Call<ReferEarnModel?>, response: Response<ReferEarnModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val referEarnModel: ReferEarnModel? = response.body()
                        if (referEarnModel != null) {
                            referEarnActivity!!.tvEarningAmount.text = String.format(
                                "%s %s",
                                getString(R.string.rs),
                                referEarnModel.results?.referralEarnings
                            )
                            referEarnActivity!!.tvTotalEarnRefer.text = String.format(
                                "%s %s",
                                getString(R.string.rs),
                                referEarnModel.results?.referralAmounts
                            )
                            referEarnActivity!!.tvReferEarnCode.text =
                                referEarnModel.results?.referCode
                            referEarnActivity!!.tvEarningLoadAmount.text = String.format(
                                "%s %s",
                                getString(R.string.rs),
                                referEarnModel.results?.minimumWalletLoadAmount
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

                override fun onFailure(p0: Call<ReferEarnModel?>, throwable: Throwable) {
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