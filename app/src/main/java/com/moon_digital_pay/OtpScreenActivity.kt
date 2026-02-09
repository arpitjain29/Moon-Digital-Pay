package com.moon_digital_pay

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityOtpScreenBinding
import com.moon_digital_pay.models.CommonModel
import com.moon_digital_pay.models.login.LoginModel
import com.moon_digital_pay.models.otp.OtpModel
import com.moon_digital_pay.parameters.ForgotPasswordParams
import com.moon_digital_pay.parameters.OtpParams
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class OtpScreenActivity : BaseActivity() {
    private var otpScreenBinding: ActivityOtpScreenBinding? = null
    private var REQUEST_CONTACT_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpScreenBinding = ActivityOtpScreenBinding.inflate(layoutInflater)
        setContentView(otpScreenBinding!!.root)

        otpScreenBinding!!.ivBackClick.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val forgotPasswordParams = ForgotPasswordParams()
        forgotPasswordParams.phoneNumber =
            AppController.instance?.sessionManager?.getLoginModel?.user?.mobile
        otpUserApi(forgotPasswordParams)

        otpScreenBinding!!.btnDone.setOnClickListener {
            val otpParams = OtpParams()
            otpParams.otp = otpScreenBinding!!.etOtp1.text.toString().trim() + otpScreenBinding!!
                .etOtp2.text.toString().trim() + otpScreenBinding!!.etOtp3.text.toString()
                .trim() + otpScreenBinding!!.etOtp4.text.toString().trim()
            if (TextUtils.isEmpty(otpParams.otp)) {
                Toast.makeText(this, getString(R.string.please_enter_your_otp), Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (mContext?.let {
                        ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.WRITE_CONTACTS
                        )
                    } != PackageManager.PERMISSION_GRANTED) {
                    mContext?.let {
                        ActivityCompat.requestPermissions(
                            it,
                            arrayOf(Manifest.permission.WRITE_CONTACTS), REQUEST_CONTACT_PERMISSION
                        )
                    }
                }
                verifyOtpUserApi(otpParams)
            }
        }

        otpScreenBinding!!.tvResendOtp.setOnClickListener {
            val params = ForgotPasswordParams()
            params.phoneNumber =
                AppController.instance?.sessionManager?.getLoginModel?.user?.mobile
            resendOtpUserApi(params)
        }

        otpScreenBinding!!.etOtp1.addTextChangedListener(
            GenericTextWatcher(
                otpScreenBinding!!.etOtp1,
                otpScreenBinding!!.etOtp2
            )
        )
        otpScreenBinding!!.etOtp2.addTextChangedListener(
            GenericTextWatcher(
                otpScreenBinding!!.etOtp2,
                otpScreenBinding!!.etOtp3
            )
        )
        otpScreenBinding!!.etOtp3.addTextChangedListener(
            GenericTextWatcher(
                otpScreenBinding!!.etOtp3,
                otpScreenBinding!!.etOtp4
            )
        )
        otpScreenBinding!!.etOtp4.addTextChangedListener(
            GenericTextWatcher(
                otpScreenBinding!!.etOtp4,
                null
            )
        )

        otpScreenBinding!!.etOtp1.setOnKeyListener(GenericKeyEvent(otpScreenBinding!!.etOtp1, null))
        otpScreenBinding!!.etOtp2.setOnKeyListener(
            GenericKeyEvent(
                otpScreenBinding!!.etOtp2,
                otpScreenBinding!!.etOtp1
            )
        )
        otpScreenBinding!!.etOtp3.setOnKeyListener(
            GenericKeyEvent(
                otpScreenBinding!!.etOtp3,
                otpScreenBinding!!.etOtp2
            )
        )
        otpScreenBinding!!.etOtp4.setOnKeyListener(
            GenericKeyEvent(
                otpScreenBinding!!.etOtp4,
                otpScreenBinding!!.etOtp3
            )
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        try {
            if (requestCode == REQUEST_CONTACT_PERMISSION) {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, proceed with saving the contact
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(this, "Permission to write contacts is required", Toast.LENGTH_SHORT).show()
                }
            }else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveContact(name: String?, mobileNumber: String?) {
        val contentResolver = contentResolver
        val contactUri = ContactsContract.RawContacts.CONTENT_URI
        val contentValues = ContentValues()

        // Insert a new empty contact
        contentValues.put(ContactsContract.RawContacts.ACCOUNT_TYPE, null as String?)
        contentValues.put(ContactsContract.RawContacts.ACCOUNT_NAME, null as String?)
        val rawContactUri = contentResolver.insert(contactUri, contentValues)
        val rawContactId = ContentUris.parseId(rawContactUri!!)

        // Insert display name
        contentValues.clear()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        contentValues.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        )
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)

        // Insert mobile number
        contentValues.clear()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        contentValues.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
        contentValues.put(
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        )
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun verifyOtpUserApi(otpParams: OtpParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).verifyOtpApi(otpParams)
            call1?.enqueue(object : Callback<LoginModel?> {
                override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val otpUser: LoginModel? = response.body()
                        if (otpUser != null) {
                            if (otpUser.status == 200) {
                                AppController.instance?.sessionManager?.getLoginModel = otpUser.data
                                AppController.instance?.sessionManager?.setLoginSession(1)
                                AppController.instance?.sessionManager?.checkLogin()
                                saveContact(otpUser.data?.user?.supportName, otpUser.data?.user?.supportNumner)
                                startActivity(Intent(mContext, HomeActivity::class.java))
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

    private fun otpUserApi(forgotPasswordParams: ForgotPasswordParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).otpApi(forgotPasswordParams)
            call1?.enqueue(object : Callback<OtpModel?> {
                override fun onResponse(call: Call<OtpModel?>, response: Response<OtpModel?>) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val otpModel: OtpModel? = response.body()
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

                override fun onFailure(p0: Call<OtpModel?>, throwable: Throwable) {
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

    private fun resendOtpUserApi(forgotPasswordParams: ForgotPasswordParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).resendOtpApi(forgotPasswordParams)
            call1?.enqueue(object : Callback<CommonModel?> {
                override fun onResponse(
                    call: Call<CommonModel?>,
                    response: Response<CommonModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val commonModel: CommonModel? = response.body()
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

                override fun onFailure(p0: Call<CommonModel?>, throwable: Throwable) {
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
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.etOtp1 && currentView.text.isEmpty()) {
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