package com.moon_digital_pay

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityLoginBinding
import com.moon_digital_pay.models.login.LoginModel
import com.moon_digital_pay.parameters.LoginParams
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LoginActivity : BaseActivity() {
    private var loginBinding: ActivityLoginBinding? = null
    var REQUEST_CONTACT_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding!!.root)

        loginBinding!!.btnLogin.setOnClickListener {
            val loginParams = LoginParams()
            loginParams.phoneNumber = loginBinding!!.etMobileNumber.text.toString().trim()
            loginParams.password = loginBinding!!.etPassword.text.toString().trim()
            loginParams.deviceToken = "1234"
            loginParams.deviceType = Constants.android

            if (TextUtils.isEmpty(loginParams.phoneNumber) ||
                loginParams.phoneNumber!!.length != 10
            ) {
                loginBinding!!.etMobileNumber.error =
                    getString(R.string.please_enter_your_mobile_number)
            } else if (TextUtils.isEmpty(loginParams.password)) {
                loginBinding!!.etPassword.error = getString(R.string.please_enter_your_password)
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
                loginApi(loginParams)
            }
        }
        loginBinding!!.llSignUp.setOnClickListener {
            startActivity(Intent(mContext, SignUpActivity::class.java))
        }
        loginBinding!!.tvForgotPassword.setOnClickListener {
            startActivity(Intent(mContext, ForgotPasswordActivity::class.java))
        }
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

    private fun loginApi(loginParams: LoginParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).loginUserApi(loginParams)
            call1?.enqueue(object : Callback<LoginModel?> {
                override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val loginUser: LoginModel? = response.body()
                        if (loginUser != null) {
                            CommonFunction.showToastSingle(mContext, loginUser.message, 0)
                            if (loginUser.status == 200) {
                                AppController.instance?.sessionManager?.getLoginModel =
                                    loginUser.data
                                AppController.instance?.sessionManager?.setLoginSession(1)
                                AppController.instance?.sessionManager?.checkLogin()
                                saveContact(loginUser.data?.user?.supportName, loginUser.data?.user?.supportNumner)
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
}