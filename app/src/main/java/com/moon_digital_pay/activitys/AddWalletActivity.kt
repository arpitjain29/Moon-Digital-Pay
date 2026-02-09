package com.moon_digital_pay.activitys

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.text.ClipboardManager
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.moon_digital_pay.HomeActivity
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.ApiUrlEndpoint
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityAddWalletBinding
import com.moon_digital_pay.models.CommonModel
import com.moon_digital_pay.models.getprofile.GetProfileModel
import com.moon_digital_pay.models.login.LoginModel
import com.moon_digital_pay.models.paymentrequest.PaymentRequestModel
import com.moon_digital_pay.parameters.AddWalletParams
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AddWalletActivity : BaseActivity() {

    private var addWalletBinding: ActivityAddWalletBinding? = null
    private var businessLogoBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addWalletBinding = ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(addWalletBinding!!.root)

        addWalletBinding!!.ivBackAdd.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        addWalletBinding!!.ivAttachment.setOnClickListener {
            showPictureDialog()
        }

        addWalletBinding!!.ivCopyText.setOnClickListener {
            mContext?.let { it1 ->
                setClipboard(
                    it1,
                    addWalletBinding!!.tvUpiNumber.text.toString()
                )
            }
        }

        addWalletBinding!!.ivCopyTextNumber.setOnClickListener {
            mContext?.let { it1 -> setClipboard(it1, addWalletBinding!!.tvNumber.text.toString()) }
        }

        addWalletBinding!!.btnSubmitAddWallet.setOnClickListener {
            val addWalletParams = AddWalletParams()
            addWalletParams.amount = addWalletBinding!!.etAmountAddWallet.text.toString().trim()
            addWalletParams.utrNumber =
                addWalletBinding!!.etUtrNumberAddWallet.text.toString().trim()

            if (TextUtils.isEmpty(addWalletParams.amount)) {
                addWalletBinding!!.etAmountAddWallet.error = getString(R.string.please_enter_amount)
            } else {
                addWalletApi(addWalletParams)
            }
        }
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

    override fun onUploadImage(imageUrl: Bitmap) {
        super.onUploadImage(imageUrl)
        businessLogoBitmap = imageUrl
        val imageuri = getImageUri(mContext!!,imageUrl)
        addWalletBinding!!.tvAttachmentFile.text = imageuri.toString()
        addWalletBinding!!.ivViewImageAttachment.visibility = View.VISIBLE
        addWalletBinding!!.ivViewImageAttachment.setImageBitmap(imageUrl)
    }

    private fun addWalletApi(addWalletParams: AddWalletParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            var businessImageBody: MultipartBody.Part? = null
            if (businessLogoBitmap != null) {
                val file = CommonFunction.persistImage(businessLogoBitmap!!, mContext!!)
                val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                businessImageBody = MultipartBody.Part.createFormData(
                    ApiUrlEndpoint.imagePath, file.name, requestFile
                )
            }

            val partMap: MutableMap<String, RequestBody> = addWalletParams.toUpdateProfileMap()

            val call = ApiClient.buildService(mContext)
                .paymentReqApi(businessImageBody, partMap)
            call?.enqueue(object : Callback<PaymentRequestModel?> {
                override fun onResponse(
                    call: Call<PaymentRequestModel?>, response: Response<PaymentRequestModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val paymentRequestModel: PaymentRequestModel? = response.body()
                        if (paymentRequestModel != null) {
                            startActivity(Intent(mContext,WalletHistoryActivity::class.java))
                            CommonFunction.showToastSingle(mContext, paymentRequestModel.message, 0)
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

                override fun onFailure(call: Call<PaymentRequestModel?>, throwable: Throwable) {
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