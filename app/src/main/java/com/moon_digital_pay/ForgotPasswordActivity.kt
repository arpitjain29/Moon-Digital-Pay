package com.moon_digital_pay

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityForgotPasswordBinding
import com.moon_digital_pay.models.forgotpassword.ForgotPasswordModel
import com.moon_digital_pay.models.login.DataLoginModel
import com.moon_digital_pay.parameters.ForgotPasswordParams
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ForgotPasswordActivity : BaseActivity() {
    private var forgotPasswordBinding: ActivityForgotPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(forgotPasswordBinding!!.root)

        forgotPasswordBinding!!.ivBackClick.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        forgotPasswordBinding!!.btnForgotPassword.setOnClickListener {
            val forgotPasswordParams = ForgotPasswordParams()
            forgotPasswordParams.phoneNumber = forgotPasswordBinding!!.etMobileNumberForgot.text.toString().trim()

            if (TextUtils.isEmpty(forgotPasswordParams.phoneNumber) ||
                forgotPasswordParams.phoneNumber!!.length != 10
            ) {
                forgotPasswordBinding!!.etMobileNumberForgot.error =
                    getString(R.string.please_enter_your_mobile_number)
            } else {
                forgotPasswordApi(forgotPasswordParams)
            }
        }
    }

    private fun forgotPasswordApi(forgotPasswordParams: ForgotPasswordParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).forgotPassword(forgotPasswordParams)
            call1?.enqueue(object : Callback<ForgotPasswordModel?> {
                override fun onResponse(call: Call<ForgotPasswordModel?>, response: Response<ForgotPasswordModel?>) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val forgotPasswordModel: ForgotPasswordModel? = response.body()
                        if (forgotPasswordModel != null) {
                            CommonFunction.showToastSingle(mContext, forgotPasswordModel.message, 0)
                            if (forgotPasswordModel.status == 200) {
                                val loginModel = DataLoginModel()
                                loginModel.accessToken = forgotPasswordModel.data?.accessToken
                                AppController.instance?.sessionManager?.getLoginModel = loginModel
                                startActivity(Intent(mContext,ResetForgotPasswordActivity::class.java))
                            }
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
                                    mContext,
                                    "An error occurred. Please try again.",
                                    0
                                )
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordModel?>, throwable: Throwable) {
                    hideProgressDialog()
                    throwable.printStackTrace()
                    if (throwable is HttpException) {
                        throwable.printStackTrace()
                    }
                }

            })
        } else {
            CommonFunction.showToastSingle(
                mContext,
                resources.getString(R.string.net_connection), 0
            )
        }
    }
}