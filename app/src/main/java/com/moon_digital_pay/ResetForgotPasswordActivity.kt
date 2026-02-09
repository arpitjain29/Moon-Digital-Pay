package com.moon_digital_pay

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityResetForgotPasswordBinding
import com.moon_digital_pay.models.CommonModel
import com.moon_digital_pay.parameters.ResetPasswordParams
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ResetForgotPasswordActivity : BaseActivity() {

    private var resetForgotPasswordBinding: ActivityResetForgotPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetForgotPasswordBinding = ActivityResetForgotPasswordBinding.inflate(layoutInflater)
        setContentView(resetForgotPasswordBinding!!.root)

        resetForgotPasswordBinding!!.btnDone.setOnClickListener {
            val resetPasswordParams = ResetPasswordParams()
            resetPasswordParams.otpCode =
                resetForgotPasswordBinding!!.etOtp1.text.toString().trim() +
                        resetForgotPasswordBinding!!.etOtp2.text.toString().trim() +
                        resetForgotPasswordBinding!!.etOtp3.text.toString().trim() +
                        resetForgotPasswordBinding!!.etOtp4.text.toString().trim()
            resetPasswordParams.newPassword =
                resetForgotPasswordBinding!!.etNewPasswordReset.text.toString().trim()
            if (TextUtils.isEmpty(resetPasswordParams.otpCode)) {
                Toast.makeText(this, getString(R.string.please_enter_your_otp), Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(resetPasswordParams.newPassword)) {
                resetForgotPasswordBinding!!.etNewPasswordReset.error =
                    getString(R.string.please_enter_new_password)
            } else if (TextUtils.isEmpty(resetForgotPasswordBinding!!.etConfirmNewPasswordReset.text)) {
                resetForgotPasswordBinding!!.etConfirmNewPasswordReset.error =
                    getString(R.string.please_enter_confirm_password)
            } else if (resetForgotPasswordBinding!!.etConfirmNewPasswordReset.text.toString()
                != resetPasswordParams.newPassword) {
                resetForgotPasswordBinding!!.etConfirmNewPasswordReset.error =
                    getString(R.string.confirm_password_does_not_match_with_password)
            } else {
                resetPasswordApi(resetPasswordParams)
            }
        }

        resetForgotPasswordBinding!!.etOtp1.addTextChangedListener(
            GenericTextWatcher(
                resetForgotPasswordBinding!!.etOtp1,
                resetForgotPasswordBinding!!.etOtp2
            )
        )
        resetForgotPasswordBinding!!.etOtp2.addTextChangedListener(
            GenericTextWatcher(
                resetForgotPasswordBinding!!.etOtp2,
                resetForgotPasswordBinding!!.etOtp3
            )
        )
        resetForgotPasswordBinding!!.etOtp3.addTextChangedListener(
            GenericTextWatcher(
                resetForgotPasswordBinding!!.etOtp3,
                resetForgotPasswordBinding!!.etOtp4
            )
        )
        resetForgotPasswordBinding!!.etOtp4.addTextChangedListener(
            GenericTextWatcher(
                resetForgotPasswordBinding!!.etOtp4,
                null
            )
        )

        resetForgotPasswordBinding!!.etOtp1.setOnKeyListener(
            GenericKeyEvent(
                resetForgotPasswordBinding!!.etOtp1,
                null
            )
        )
        resetForgotPasswordBinding!!.etOtp2.setOnKeyListener(
            GenericKeyEvent(
                resetForgotPasswordBinding!!.etOtp2,
                resetForgotPasswordBinding!!.etOtp1
            )
        )
        resetForgotPasswordBinding!!.etOtp3.setOnKeyListener(
            GenericKeyEvent(
                resetForgotPasswordBinding!!.etOtp3,
                resetForgotPasswordBinding!!.etOtp2
            )
        )
        resetForgotPasswordBinding!!.etOtp4.setOnKeyListener(
            GenericKeyEvent(
                resetForgotPasswordBinding!!.etOtp4,
                resetForgotPasswordBinding!!.etOtp3
            )
        )
    }

    private fun resetPasswordApi(resetPasswordParams: ResetPasswordParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).resetPassword(resetPasswordParams)
            call1?.enqueue(object : Callback<CommonModel?> {
                override fun onResponse(
                    call: Call<CommonModel?>,
                    response: Response<CommonModel?>
                ) {
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

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL
                && currentView.id != R.id.etOtp1 && currentView.text.isEmpty()
            ) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor
        (private val currentView: View, private val nextView: View?) : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (currentView.id) {
                R.id.etOtp1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etOtp2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etOtp3 -> if (text.length == 1) nextView!!.requestFocus()
                //You can use EditText4 same as above to hide the keyboard
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) {
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) {
        }
    }
}