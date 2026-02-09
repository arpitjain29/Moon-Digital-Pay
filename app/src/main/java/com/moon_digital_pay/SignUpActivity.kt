package com.moon_digital_pay

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.location.Location
import android.os.Bundle
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivitySignUpBinding
import com.moon_digital_pay.models.login.LoginModel
import com.moon_digital_pay.parameters.SignupParams
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.LocationInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class SignUpActivity : BaseActivity(), View.OnClickListener {
    private var signUpBinding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding!!.root)

        signUpBinding!!.ivBackClick.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        signUpBinding!!.btnSignUp.setOnClickListener(this)
        signUpBinding!!.llLogin.setOnClickListener(this)
        initLiveLocation(null)
    }

    override fun onClick(p0: View?) {
        super.onClick(p0)
        CommonFunction.hideKeyboardFrom(mContext!!, p0!!)
        when (p0.id) {
            R.id.btnSignUp -> {
                initLiveLocation(object : LocationInterface {
                    override fun fetchLocation(location: Location?) {
                        if (location != null) {
                            val signupParams = SignupParams()
                            signupParams.name = signUpBinding!!.etFullName.text.toString().trim()
                            signupParams.email = signUpBinding!!.etEmailId.text.toString().trim()
                            signupParams.mobile =
                                signUpBinding!!.etMobileNumber.text.toString().trim()
                            signupParams.password =
                                signUpBinding!!.etPassword.text.toString().trim()
                            signupParams.address = signUpBinding!!.etAddress.text.toString().trim()
                            signupParams.latitude = location.latitude.toString()
                            signupParams.longitude = location.longitude.toString()
                            signupParams.pincode = "+91"
                            signupParams.deviceId = CommonFunction.getDeviceId(mContext!!)
                            signupParams.deviceToken = "1234"
                            signupParams.deviceType = Constants.android

                            if (TextUtils.isEmpty(signupParams.name)) {
                                signUpBinding!!.etFullName.error =
                                    getString(R.string.please_enter_full_name)
                            } else if (TextUtils.isEmpty(signupParams.email)
                                || CommonFunction.emailValidator(signUpBinding!!.etEmailId.text.toString())
                            ) {
                                signUpBinding!!.etEmailId.error =
                                    getString(R.string.please_enter_your_email_id)
                            } else if (TextUtils.isEmpty(signupParams.mobile) ||
                                signupParams.mobile!!.length != 10
                            ) {
                                signUpBinding!!.etMobileNumber.error =
                                    getString(R.string.please_enter_your_mobile_number)
                            } else if (TextUtils.isEmpty(signupParams.password)) {
                                signUpBinding!!.etPassword.error =
                                    getString(R.string.please_enter_your_password)
                            } else if (TextUtils.isEmpty(signupParams.address)) {
                                signUpBinding!!.etAddress.error =
                                    getString(R.string.please_enter_your_address)
                            } else {
                                signupApi(signupParams)
                            }
                        }
                    }
                })
            }

            R.id.llLogin -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun signupApi(signupParams: SignupParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).signupUserApi(signupParams)
            call1?.enqueue(object : Callback<LoginModel?> {
                override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val loginModel: LoginModel? = response.body()
                        if (loginModel != null) {
                            if (loginModel.status == 200) {
                                AppController.instance?.sessionManager?.getLoginModel =
                                    loginModel.data
                                startActivity(
                                    Intent(
                                        mContext,
                                        OtpScreenActivity::class.java
                                    )
                                )
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

                override fun onFailure(call: Call<LoginModel?>, throwable: Throwable) {
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