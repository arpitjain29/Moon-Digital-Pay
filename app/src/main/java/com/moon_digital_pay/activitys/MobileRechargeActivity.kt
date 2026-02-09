package com.moon_digital_pay.activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.moon_digital_pay.R
import com.moon_digital_pay.adapter.MobileRechargeAdapter
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityMobileRechargeBinding
import com.moon_digital_pay.models.CommonModel
import com.moon_digital_pay.models.checkBalance.CheckBalanceModel
import com.moon_digital_pay.models.checkRechargeROfferPlan.CheckRechargeROfferPlanModel
import com.moon_digital_pay.models.checkoperator.CheckOperatorModel
import com.moon_digital_pay.models.checkrechargeplan.CheckRechargePlanModel
import com.moon_digital_pay.models.checkrechargeplan.PlansCheckRechargePlanModel
import com.moon_digital_pay.models.operatorlist.DataOperatorListModel
import com.moon_digital_pay.models.regions.RegionsItemModel
import com.moon_digital_pay.parameters.CheckOperatorParams
import com.moon_digital_pay.parameters.CheckRechargePlanParams
import com.moon_digital_pay.parameters.CheckRechargeROfferPlanParams
import com.moon_digital_pay.parameters.MobileRechargeParams
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MobileRechargeActivity : BaseActivity() {

    private var mobileRechargeBinding: ActivityMobileRechargeBinding? = null
    private val REQUEST_READ_CONTACTS: Int = 79
    private var selectPlan: PlansCheckRechargePlanModel? = null
    var selectOperator: DataOperatorListModel? = null
    var selectCircle: RegionsItemModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mobileRechargeBinding = ActivityMobileRechargeBinding.inflate(layoutInflater)
        setContentView(mobileRechargeBinding!!.root)

        mobileRechargeBinding!!.ivBackMobile.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mobileRechargeBinding!!.tvPlanDetails.setOnClickListener {
            if (selectOperator == null) {
                CommonFunction.showToastSingle(
                    mContext, resources.getString(R.string.please_select_operator), 0
                )
            } else {
                planResultLauncher.launch(
                    Intent(mContext, PlanDetailsActivity::class.java).putExtra(
                        Constants.idIntent, selectOperator?.id
                    )
                )
            }
        }

        mobileRechargeBinding!!.btnSubmitMobileRecharge.setOnClickListener {
            if (TextUtils.isEmpty(mobileRechargeBinding!!.etMobileNumber.text.toString())) {
                CommonFunction.showToastSingle(
                    mContext, resources.getString(R.string.please_enter_your_mobile_number), 0
                )
            } else if (selectOperator == null) {
                CommonFunction.showToastSingle(
                    mContext, resources.getString(R.string.please_select_operator), 0
                )
            } else {
                val mobileRechargeParams = MobileRechargeParams()
                mobileRechargeParams.phoneNumber =
                    mobileRechargeBinding!!.etMobileNumber.text.toString().trim()
                mobileRechargeParams.operatorId = selectOperator?.id.toString()
                mobileRechargeParams.amount = if (selectPlan == null){
                     mobileRechargeBinding!!.etAmount.text.toString().trim()
                }else{
                    selectPlan?.amount
                }
                mobileRechargeApi(mobileRechargeParams)
            }

        }
        mobileRechargeBinding!!.etOperatorName.setOnClickListener {
            operatorResultLauncher.launch(
                Intent(mContext, OperatorListActivity::class.java).putExtra(
                    Constants.typeIntent, Constants.mobile
                ).putExtra(
                    Constants.fromWhereIntent, Constants.operatorIntent
                )
            )
        }
        mobileRechargeBinding!!.etCircleName.setOnClickListener {
            operatorResultLauncher.launch(
                Intent(mContext, OperatorListActivity::class.java).putExtra(
                    Constants.fromWhereIntent, Constants.circleIntent
                )
            )
        }

        mobileRechargeBinding!!.ivMobilePhone.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                someActivityResultLauncher.launch(intent)
            } else {
                requestPermission()
            }
        }

        mobileRechargeBinding!!.etMobileNumber.addTextChangedListener(textWatcher)

        dashboardCheckBalanceApi()
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val mobileNumber: String = mobileRechargeBinding!!.etMobileNumber.text.toString().trim()
            if (android.util.Patterns.PHONE.matcher(mobileNumber)
                    .matches() && mobileNumber.length >= 10
            ) {
                println("mobile number =====     $mobileNumber")
                val checkOperatorParams = CheckOperatorParams()
                checkOperatorParams.number = mobileNumber
                checkOperatorParams.operatorType = Constants.mobile
                checkOperatorApi(checkOperatorParams)
                if (!TextUtils.isEmpty(
                        mobileRechargeBinding!!.etMobileNumber.text.toString().trim()
                    ) && selectOperator != null
                ) {
                    val checkRechargeROfferPlanParams = CheckRechargeROfferPlanParams()
                    checkRechargeROfferPlanParams.operatorId = selectOperator?.id
                    checkRechargeROfferPlanParams.mobileNo =
                        mobileRechargeBinding!!.etMobileNumber.text.toString().trim()
                    checkRechargeROfferPlan(checkRechargeROfferPlanParams)
                }
            }
        }

        override fun afterTextChanged(s: Editable) {
        }
    }

    private var someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val uri = data?.data

            uri.let { contactData ->
                val cursor = contactData?.let {
                    contentResolver.query(
                        it, null, null, null, null
                    )
                }

                cursor?.use {
                    if (it.moveToFirst()) {
                        val contactId =
                            it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val displayName =
                            it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                        val hasPhoneNumber =
                            it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        if (hasPhoneNumber > 0) {
                            val phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                                arrayOf(contactId),
                                null
                            )

                            phoneCursor?.use { pCursor ->
                                if (pCursor.moveToFirst()) {
                                    val phoneNumber = pCursor.getString(
                                        pCursor.getColumnIndexOrThrow(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER
                                        )
                                    )

                                    val number = phoneNumber.replaceFirst("^\\+91|^0+".toRegex(), "").replace("[\\s-]".toRegex(), "")

                                    mobileRechargeBinding!!.etMobileNumber.text = Editable.Factory.getInstance().newEditable(number)

                                    val checkOperatorParams = CheckOperatorParams()
                                    checkOperatorParams.number = number
                                    checkOperatorParams.operatorType = Constants.mobile
                                    checkOperatorApi(checkOperatorParams)
                                }
                            }
                        }

                        mobileRechargeBinding!!.tvCustomerName.text = displayName
//                        val operatorName = getOperatorName()
//                        val circleName = getCircleName()


                        // Set the operator name in a TextView
//                        mobileRechargeBinding!!.etOperatorName.text =
//                            Editable.Factory.getInstance().newEditable(operatorName)
//                        // Set the operator logo in an ImageView
////                        mobileRechargeBinding?.ivOperatorImage?.visibility = View.VISIBLE
////                        mobileRechargeBinding!!.ivOperatorImage.setImageResource(operatorLogoResId)
//
//                        mobileRechargeBinding!!.etCircleName.text =
//                            Editable.Factory.getInstance().newEditable(circleName)
                    }
                }
            }
        }
    }


    private var operatorResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data?.getStringExtra(Constants.fromWhereIntent).equals(Constants.operatorIntent)) {
                selectOperator =
                    data?.getSerializableExtra(Constants.modelIntent) as DataOperatorListModel
                setOperator()
            } else if (data?.getStringExtra(Constants.fromWhereIntent)
                    .equals(Constants.circleIntent)
            ) {

                selectCircle = data?.getSerializableExtra(Constants.modelIntent) as RegionsItemModel
                mobileRechargeBinding!!.etCircleName.setText(selectCircle?.stateName)

            }

        }
    }


    private var planResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            selectPlan =
                data?.getSerializableExtra(Constants.modelIntent) as PlansCheckRechargePlanModel
            mobileRechargeBinding?.etAmount?.setText(selectPlan?.amount)

        }
    }


//    private fun getCircleName(): String {
//        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        val simOperator = telephonyManager.simOperator // MCC-MNC
//        val circleMap = mapOf(
//            "40401" to "Delhi",
//            "40402" to "Kolkata",
//            "40403" to "Maharashtra",
//            "40404" to "Mumbai",
//            "40405" to "Gujarat",
//            "40407" to "UP West",
//            "40409" to "Punjab",
//            "40410" to "Haryana",
//            "40411" to "UP East",
//            "40412" to "Himachal Pradesh",
//            "40413" to "Jammu & Kashmir",
//            "40414" to "Rajasthan",
//            "40415" to "Madhya Pradesh",
//            "40416" to "West Bengal",
//            "40417" to "Kerala",
//            "40418" to "Karnataka",
//            "40419" to "Andhra Pradesh",
//            "40420" to "Tamil Nadu",
//            "40421" to "Chennai",
//            "40422" to "UP West",
//            "40424" to "Orissa",
//            "40425" to "Assam",
//            "40427" to "North East",
//            "40430" to "Bihar",
//            "40431" to "Kolkata",
//            "40434" to "Himachal Pradesh",
//            "40436" to "Jammu & Kashmir",
//            "40437" to "Assam",
//            "40438" to "North East",
//            "40440" to "Tamil Nadu",
//            "40441" to "Chennai",
//            "40442" to "Maharashtra",
//            "40443" to "Mumbai",
//            "40444" to "Gujarat",
//            "40445" to "Karnataka",
//            "40446" to "Andhra Pradesh",
//            "40449" to "Punjab",
//            "40450" to "Kerala",
//            "40451" to "Haryana",
//            "40452" to "Haryana",
//            "40453" to "UP East",
//            "40454" to "UP West",
//            "40455" to "Rajasthan",
//            "40456" to "Punjab",
//            "40457" to "West Bengal",
//            "40458" to "Madhya Pradesh",
//            "40459" to "Himachal Pradesh",
//            "40460" to "UP East",
//            "40462" to "Orissa",
//            "40464" to "North East",
//            "40466" to "Assam",
//            "40467" to "Gujarat",
//            "40468" to "Maharashtra",
//            "40469" to "Mumbai",
//            "40470" to "Madhya Pradesh",
//            "40471" to "Rajasthan",
//            "40472" to "Kerala",
//            "40473" to "Karnataka",
//            "40474" to "Andhra Pradesh",
//            "40475" to "Chennai",
//            "40476" to "UP West",
//            "40477" to "West Bengal",
//            "40478" to "Bihar",
//            "40479" to "Assam",
//            "40480" to "North East",
//            "40481" to "UP East",
//            "40482" to "UP West",
//            "40483" to "Madhya Pradesh",
//            "40484" to "Rajasthan",
//            "40485" to "Punjab",
//            "40486" to "Haryana",
//            "40487" to "Bihar",
//            "40488" to "Orissa",
//            "40489" to "Jammu & Kashmir",
//            "40490" to "Karnataka",
//            "40491" to "Kerala",
//            "40492" to "Chennai",
//            "40493" to "Haryana",
//            "40494" to "UP East",
//            "40495" to "Tamil Nadu",
//            "40496" to "UP West",
//            "40497" to "Bihar",
//            "40498" to "Himachal Pradesh"
//        )
//
//        return circleMap[simOperator] ?: "Unknown Circle"
//    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_CONTACTS
            )
        ) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS
            )
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_CONTACTS
            )
        ) {
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        try {
            if (requestCode == REQUEST_READ_CONTACTS) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    locationInterface!!.fetchLocation(null)
//                    getLastLocation()
                } else {
//                    checkUserRequestedDontAskAgain()
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dashboardCheckBalanceApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).checkBalanceApi()
            call1?.enqueue(object : Callback<CheckBalanceModel?> {
                override fun onResponse(
                    call: Call<CheckBalanceModel?>, response: Response<CheckBalanceModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val checkBalanceModel: CheckBalanceModel? = response.body()
                        if (checkBalanceModel != null) {
                            if (checkBalanceModel.status == 200) {
                                mobileRechargeBinding!!.cbCheckBalance.text = String.format(
                                    "%s %s %s",
                                    resources.getString(R.string.wallet_balance_),
                                    getString(R.string.rs),
                                    checkBalanceModel.results?.wallet
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
                                    mContext, "An error occurred. Please try again.", 0
                                )
                            }
                        }
                    }
                }

                override fun onFailure(p0: Call<CheckBalanceModel?>, throwable: Throwable) {
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

//    private fun operatorListApi() {
//        if (isConnectingToInternet(mContext!!)) {
//            showProgressDialog()
//            val call1 = ApiClient.buildService(mContext).operatorListApi(Constants.mobile)
//            call1?.enqueue(object : Callback<OperatorListModel?> {
//                override fun onResponse(
//                    call: Call<OperatorListModel?>, response: Response<OperatorListModel?>
//                ) {
//                    hideProgressDialog()
//                    if (response.isSuccessful) {
//                        val operatorListModel: OperatorListModel? = response.body()
//                        if (operatorListModel != null) {
//
//                        }
//                    } else {
//                        val errorBody = response.errorBody()?.string()
//                        if (errorBody != null) {
//                            try {
//                                val errorJson = JSONObject(errorBody)
//                                val errorArray = errorJson.getJSONArray("error")
//                                val errorMessage = errorArray.getJSONObject(0).getString("message")
//                                CommonFunction.showToastSingle(mContext, errorMessage, 0)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                                CommonFunction.showToastSingle(
//                                    mContext, "An error occurred. Please try again.", 0
//                                )
//                            }
//                        }
//                    }
//                }
//
//                override fun onFailure(p0: Call<OperatorListModel?>, throwable: Throwable) {
//                    hideProgressDialog()
//                    throwable.printStackTrace()
//                    if (throwable is HttpException) {
//                        throwable.printStackTrace()
//                    }
//                }
//
//            })
//        } else {
//            CommonFunction.showToastSingle(
//                mContext, resources.getString(R.string.net_connection), 0
//            )
//        }
//    }

    private fun checkRechargePlanApi(checkRechargePlanParams: CheckRechargePlanParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 =
                ApiClient.buildService(mContext).checkRechargePlanApi(checkRechargePlanParams)
            call1?.enqueue(object : Callback<CheckRechargePlanModel?> {
                override fun onResponse(
                    call: Call<CheckRechargePlanModel?>, response: Response<CheckRechargePlanModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val checkRechargePlanModel: CheckRechargePlanModel? = response.body()
                        if (checkRechargePlanModel != null) {

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

                override fun onFailure(p0: Call<CheckRechargePlanModel?>, throwable: Throwable) {
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

    private fun checkRechargeROfferPlan(checkRechargeROfferPlanParams: CheckRechargeROfferPlanParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext)
                .checkRechargeROfferPlanApi(checkRechargeROfferPlanParams)
            call1?.enqueue(object : Callback<CheckRechargeROfferPlanModel?> {
                override fun onResponse(
                    call: Call<CheckRechargeROfferPlanModel?>,
                    response: Response<CheckRechargeROfferPlanModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val checkRechargeROfferPlanModel: CheckRechargeROfferPlanModel? =
                            response.body()
                        if (checkRechargeROfferPlanModel?.results?.isNotEmpty() == true) {
                            mobileRechargeBinding?.llOfferLayoutMobileRecharge?.visibility =
                                View.VISIBLE
                            val mobileRechargeAdapter = MobileRechargeAdapter(
                                checkRechargeROfferPlanModel.results, mContext!!
                            )
                            mobileRechargeBinding!!.rvMobileRecharge.adapter = mobileRechargeAdapter
                        } else {
                            mobileRechargeBinding?.llOfferLayoutMobileRecharge?.visibility =
                                View.GONE

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

                override fun onFailure(
                    p0: Call<CheckRechargeROfferPlanModel?>, throwable: Throwable
                ) {
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

    private fun mobileRechargeApi(mobileRechargeParams: MobileRechargeParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).mobileRechargeApi(mobileRechargeParams)
            call1?.enqueue(object : Callback<CommonModel> {
                override fun onResponse(
                    call: Call<CommonModel?>, response: Response<CommonModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val commonModel: CommonModel? = response.body()
                        CommonFunction.showToastSingle(
                            mContext, commonModel?.message, 0
                        )

                        if (commonModel?.status == 200) {
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

                override fun onFailure(
                    p0: Call<CommonModel?>, throwable: Throwable
                ) {
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

    private fun checkOperatorApi(checkOperatorParams: CheckOperatorParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).checkOperatorApi(checkOperatorParams)
            call1?.enqueue(object : Callback<CheckOperatorModel?> {
                override fun onResponse(
                    call: Call<CheckOperatorModel?>, response: Response<CheckOperatorModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val checkOperatorModel: CheckOperatorModel? = response.body()
                        if (checkOperatorModel != null) {
                            selectOperator = checkOperatorModel.results
                            selectCircle = checkOperatorModel.circle

                            mobileRechargeBinding!!.etCircleName.setText(selectCircle?.stateName)

                            setOperator()
//                            val checkRechargePlanParams = CheckRechargePlanParams()
//                            checkRechargePlanParams.operatorId = checkOperatorModel.results?.id
//                            checkRechargePlanParams.operatorType = Constants.mobile
//                            checkRechargePlanParams.operatorCircle =
//                                checkOperatorModel.circle?.circleCode
//                            checkRechargePlanApi(checkRechargePlanParams)

//                            val checkRechargeROfferPlanParams = CheckRechargeROfferPlanParams()
//                            checkRechargeROfferPlanParams.operatorId =
//                                selectOperator?.id
//                            checkRechargeROfferPlanParams.mobileNo =
//                                mobileRechargeBinding!!.etMobileNumber.text.toString().trim()
//                            checkRechargeROfferPlan(checkRechargeROfferPlanParams)

//                            mobileRechargeBinding!!.etOperatorName.text =
//                                Editable.Factory.getInstance()
//                                    .newEditable(checkOperatorModel.results?.operatorName)
//                            mobileRechargeBinding?.ivOperatorImage?.visibility = View.VISIBLE
//                            checkOperatorModel.results?.imageUrl?.let {
//                                CommonFunction.loadImageViaGlide(
//                                    mContext,
//                                    it,
//                                    mobileRechargeBinding!!.ivOperatorImage,
//                                    R.drawable.ic_placeholer_image
//                                )
//                            }
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

                override fun onFailure(p0: Call<CheckOperatorModel?>, throwable: Throwable) {
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


    fun setOperator() {
        mobileRechargeBinding?.etOperatorName?.setText(selectOperator?.operatorName)
        mContext?.let {
            Glide.with(it).load(selectOperator?.imageUrl).apply(RequestOptions().fitCenter())
                .into(object : CustomTarget<Drawable>(50, 50) {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        mobileRechargeBinding?.etOperatorName?.setCompoundDrawablesWithIntrinsicBounds(
                            placeholder,
                            null,
                            ContextCompat.getDrawable(mContext!!, R.drawable.ic_arrow_drop_down),
                            null
                        )
                    }

                    override fun onResourceReady(
                        resource: Drawable, transition: Transition<in Drawable>?
                    ) {
                        mobileRechargeBinding?.etOperatorName?.setCompoundDrawablesWithIntrinsicBounds(
                            resource,
                            null,
                            ContextCompat.getDrawable(mContext!!, R.drawable.ic_arrow_drop_down),
                            null
                        )
                    }
                })

            if (!TextUtils.isEmpty(
                    mobileRechargeBinding!!.etMobileNumber.text.toString().trim()
                ) && selectOperator != null
            ) {
                val checkRechargeROfferPlanParams = CheckRechargeROfferPlanParams()
                checkRechargeROfferPlanParams.operatorId = selectOperator?.id
                checkRechargeROfferPlanParams.mobileNo =
                    mobileRechargeBinding!!.etMobileNumber.text.toString().trim()
                checkRechargeROfferPlan(checkRechargeROfferPlanParams)
            }
        }
    }
}