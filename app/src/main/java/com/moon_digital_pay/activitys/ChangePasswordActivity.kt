package com.moon_digital_pay.activitys

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.moon_digital_pay.LoginActivity
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityChangePasswordBinding
import com.moon_digital_pay.models.CommonModel
import com.moon_digital_pay.parameters.ChangePasswordParams
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ChangePasswordActivity : BaseActivity() {

    private var changePasswordActivity: ActivityChangePasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordActivity = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordActivity!!.root)

        changePasswordActivity!!.ivBackChange.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        changePasswordActivity!!.btnSubmitCharge.setOnClickListener {
            val changePasswordParams = ChangePasswordParams()
            changePasswordParams.currentPassword = changePasswordActivity!!.etOldPassword.text.toString().trim()
            changePasswordParams.newPassword = changePasswordActivity!!.etNewPassword.text.toString().trim()
            changePasswordParams.confirmPassword = changePasswordActivity!!.etConfirmNewPassword.text.toString().trim()

            if (TextUtils.isEmpty(changePasswordParams.currentPassword)){
                changePasswordActivity!!.etOldPassword.error = getString(R.string.please_enter_old_password)
            }else if (TextUtils.isEmpty(changePasswordParams.newPassword)){
                changePasswordActivity!!.etNewPassword.error = getString(R.string.please_enter_new_password)
            }else if (TextUtils.isEmpty(changePasswordParams.confirmPassword)){
                changePasswordActivity!!.etConfirmNewPassword.error = getString(R.string.please_enter_confirm_password)
            }else if (!TextUtils.isEmpty(changePasswordParams.confirmPassword) && changePasswordParams.confirmPassword != changePasswordParams.newPassword){
                changePasswordActivity!!.etConfirmNewPassword.error = getString(R.string.confirm_password_does_not_match_with_password)
            }else{
                changePassword(changePasswordParams)
            }
        }
    }

    private fun changePassword(changePasswordParams: ChangePasswordParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).changePasswordApi(changePasswordParams)
            call1?.enqueue(object : Callback<CommonModel?> {
                override fun onResponse(call: Call<CommonModel?>, response: Response<CommonModel?>) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val commonModel: CommonModel? = response.body()
                        if (commonModel != null) {
                            CommonFunction.showToastSingle(mContext, commonModel.message, 0)
                            if (commonModel.status == 200) {
                                startActivity(Intent(mContext, LoginActivity::class.java))
                                finish()
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

                override fun onFailure(call: Call<CommonModel?>, throwable: Throwable) {
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