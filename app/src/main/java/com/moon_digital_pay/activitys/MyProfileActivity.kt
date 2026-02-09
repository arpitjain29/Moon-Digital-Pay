package com.moon_digital_pay.activitys

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityMyProfileBinding
import com.moon_digital_pay.models.getprofile.GetProfileModel
import com.moon_digital_pay.models.updateprofile.UpdateProfileModel
import com.moon_digital_pay.parameters.UpdateProfileParams
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MyProfileActivity : BaseActivity() {

    private var myProfileBinding: ActivityMyProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myProfileBinding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(myProfileBinding!!.root)

        myProfileBinding!!.ivBackProfile.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        myProfileBinding!!.btnSubmitProfile.setOnClickListener {
            val updateProfileParams = UpdateProfileParams()
            updateProfileParams.name = myProfileBinding!!.etFullNameProfile.text.toString().trim()
            updateProfileParams.email =
                myProfileBinding!!.etMobileNumberProfile.text.toString().trim()
            updateProfileParams.address = myProfileBinding!!.etAddressProfile.text.toString().trim()

            if (TextUtils.isEmpty(updateProfileParams.name)) {
                myProfileBinding!!.etFullNameProfile.error =
                    getString(R.string.please_enter_full_name)
            } else if (TextUtils.isEmpty(updateProfileParams.email)) {
                myProfileBinding!!.etMobileNumberProfile.error =
                    getString(R.string.please_enter_your_email_id)
            } else if (TextUtils.isEmpty(updateProfileParams.address)) {
                myProfileBinding!!.etAddressProfile.error =
                    getString(R.string.please_enter_your_address)
            } else {
                updateApi(updateProfileParams)
            }
        }
        profileApi()
    }

    private fun profileApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).profileApi()
            call1?.enqueue(object : Callback<GetProfileModel?> {
                override fun onResponse(
                    call: Call<GetProfileModel?>, response: Response<GetProfileModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val getProfileModel: GetProfileModel? = response.body()
                        if (getProfileModel != null) {
                            myProfileBinding!!.etFullNameProfile.text =
                                Editable.Factory.getInstance().newEditable(getProfileModel.data[0].fullName)
                            myProfileBinding!!.etMobileNumberProfile.text =
                                Editable.Factory.getInstance().newEditable(getProfileModel.data[0].mobile)
                            myProfileBinding!!.etAddressProfile.text =
                                Editable.Factory.getInstance().newEditable(getProfileModel.data[0].address)
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

                override fun onFailure(p0: Call<GetProfileModel?>, throwable: Throwable) {
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

    private fun updateApi(updateProfileParams: UpdateProfileParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).updateProfileApi(updateProfileParams)
            call1?.enqueue(object : Callback<UpdateProfileModel?> {
                override fun onResponse(
                    call: Call<UpdateProfileModel?>, response: Response<UpdateProfileModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val updateProfileModel: UpdateProfileModel? = response.body()
                        if (updateProfileModel != null) {

                            val sessionModel = AppController.instance?.sessionManager?.getLoginModel
                            val sessionData = sessionModel?.user
                            sessionData?.imageUrl = updateProfileModel.data?.imageUrl
                            sessionData?.fullName = updateProfileModel.data?.fullName
                            sessionData?.mobile = updateProfileModel.data?.mobile
                            sessionModel?.user = sessionData

                            AppController.instance?.sessionManager?.getLoginModel = sessionModel
                            onBackPressedDispatcher.onBackPressed()
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

                override fun onFailure(p0: Call<UpdateProfileModel?>, throwable: Throwable) {
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