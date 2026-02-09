package com.moon_digital_pay.activitys

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.moon_digital_pay.R
import com.moon_digital_pay.adapter.MarginsAdapter
import com.moon_digital_pay.adapter.RechargeHistoryAdapter
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityRechargeHistoryBinding
import com.moon_digital_pay.databinding.FilterLayoutBinding
import com.moon_digital_pay.models.margin.MarginModel
import com.moon_digital_pay.models.rechargehistory.DatumRechargeHistoryModel
import com.moon_digital_pay.models.rechargehistory.RechargeHistoryModel
import com.moon_digital_pay.parameters.RechargeHistoryParams
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.OrderHistoryInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RechargeHistoryActivity : BaseActivity() {

    private var rechargeHistoryActivity: ActivityRechargeHistoryBinding? = null
    private var marginsAdapter: MarginsAdapter? = null
    private var rechargeHistoryAdapter: RechargeHistoryAdapter? = null
    private val calendar = Calendar.getInstance()
    private var selectCheckId: String? = null
    private var selectStartDate: String? = null
    private var selectEndDate: String? = null
    private var formattedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rechargeHistoryActivity = ActivityRechargeHistoryBinding.inflate(layoutInflater)
        setContentView(rechargeHistoryActivity!!.root)

        rechargeHistoryActivity!!.ivBackRecharge.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        rechargeHistoryActivity!!.llStartDate.setOnClickListener {
            showDatePicker("1")
        }

        rechargeHistoryActivity!!.llEndStart.setOnClickListener {
            showDatePicker("2")
        }

        rechargeHistoryActivity!!.ivCloseRecharge.setOnClickListener {
            rechargeHistoryActivity!!.tvStartDate.text = getString(R.string.start_date)
            rechargeHistoryActivity!!.tvEndDate.text = getString(R.string.end_date)
            formattedDate = ""
            val rechargeHistoryParams = RechargeHistoryParams()
            rechargeHistoryParams.startDate = ""
            rechargeHistoryParams.endDate = ""
            rechargeHistoryParams.userId =
                AppController.instance?.sessionManager?.getLoginModel?.user?.id.toString()
            rechargeHistoryParams.status = ""
            rechargeHistoryApi(rechargeHistoryParams)
        }

        rechargeHistoryActivity!!.tvRechargeGo.setOnClickListener {
            val rechargeHistoryParams = RechargeHistoryParams()
            rechargeHistoryParams.startDate = selectStartDate
            rechargeHistoryParams.endDate = selectEndDate
            rechargeHistoryParams.userId =
                AppController.instance?.sessionManager?.getLoginModel?.user?.id.toString()
            rechargeHistoryParams.status = ""
            rechargeHistoryApi(rechargeHistoryParams)
        }

        rechargeHistoryActivity!!.ivFilter.setOnClickListener {
            val dialog = Dialog(mContext!!, R.style.CustomAlertDialogStyle_space)
            if (dialog.window != null) {
                dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setGravity(Gravity.CENTER)
            }
            if (dialog.window != null) {
                dialog.window!!.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(
                        Color.TRANSPARENT
                    )
                )
            }
            dialog.setCancelable(false)
            val binding: FilterLayoutBinding = FilterLayoutBinding.inflate(
                LayoutInflater.from(
                    mContext
                ), null, false
            )
            dialog.setContentView(binding.root)
//            binding.tvMessageTextPopup.text =
//                resources.getString(R.string.are_you_sure_you_want_to_logout)
            binding.rgBtnFilter.setOnCheckedChangeListener { _, checkId ->
                when (checkId) {
                    R.id.rbAllFilter -> {
                        selectCheckId = "all"
                    }

                    R.id.rbSuccessFilter -> {
                        selectCheckId = "pending"
                    }

                    R.id.rbPendingFilter -> {
                        selectCheckId = "success"
                    }

                    R.id.rbFailedFilter -> {
                        selectCheckId = "failed"
                    }
                }
            }
            binding.tvOkTextPopup.setOnClickListener {
                val rechargeHistoryParams = RechargeHistoryParams()
                rechargeHistoryParams.startDate = ""
                rechargeHistoryParams.endDate = ""
                rechargeHistoryParams.userId =
                    AppController.instance?.sessionManager?.getLoginModel?.user?.id.toString()
                rechargeHistoryParams.status = selectCheckId
                rechargeHistoryApi(rechargeHistoryParams)
                dialog.dismiss()
            }
            binding.tvCancelTextPopup.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        if (intent.getStringExtra(Constants.screenTypes).equals(Constants.recharge)) {
            rechargeHistoryActivity!!.tvTitleBar.text = getString(R.string.recharge_history)
            rechargeHistoryActivity!!.cvDateRecharge.visibility = View.VISIBLE
            rechargeHistoryActivity!!.ivFilter.visibility = View.VISIBLE

            val rechargeHistoryParams = RechargeHistoryParams()
            rechargeHistoryParams.startDate = selectStartDate
            rechargeHistoryParams.endDate = selectEndDate
            rechargeHistoryParams.userId =
                AppController.instance?.sessionManager?.getLoginModel?.user?.id.toString()
            rechargeHistoryParams.status = selectCheckId
            rechargeHistoryApi(rechargeHistoryParams)

        } else if (intent.getStringExtra(Constants.screenTypes).equals(Constants.margins)) {
            rechargeHistoryActivity!!.tvTitleBar.text = getString(R.string.margin)
            rechargeHistoryActivity!!.cvDateRecharge.visibility = View.GONE
            rechargeHistoryActivity!!.ivFilter.visibility = View.GONE

            marginApi()
        }
    }

    private fun showDatePicker(valueDate: String) {
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                formattedDate = dateFormat.format(selectedDate.time)

                val dateFormatValue = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDateValue = dateFormatValue.format(selectedDate.time)

                if (valueDate == "1") {
                    rechargeHistoryActivity!!.tvStartDate.text = formattedDate
                    selectStartDate = formattedDateValue
                } else {
                    rechargeHistoryActivity!!.tvEndDate.text = formattedDate
                    selectEndDate = formattedDateValue
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun rechargeHistoryApi(rechargeHistoryParams: RechargeHistoryParams) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).rechargeHistoryApi(rechargeHistoryParams)
            call1?.enqueue(object : Callback<RechargeHistoryModel?> {
                override fun onResponse(
                    call: Call<RechargeHistoryModel?>, response: Response<RechargeHistoryModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val rechargeHistoryModel: RechargeHistoryModel? = response.body()
                        if (rechargeHistoryModel != null) {
                            if (rechargeHistoryModel.results?.transaction?.data?.isNotEmpty() == true) {
                                rechargeHistoryAdapter =
                                    rechargeHistoryModel.results?.transaction?.data?.let {
                                        RechargeHistoryAdapter(
                                            it, mContext!!, object : OrderHistoryInterface {
                                                override fun orderHistoryClick(
                                                    data: DatumRechargeHistoryModel,
                                                    position: Int?
                                                ) {
                                                    val intent = Intent(
                                                        mContext,
                                                        HistoryActivity::class.java
                                                    )
                                                    intent.putExtra(
                                                        "listOrderHistory",
                                                        data
                                                    )
                                                    intent.putExtra("screenIntent","recharge")
                                                    startActivity(intent)
                                                }
                                            }
                                        )
                                    }
                                rechargeHistoryActivity!!.rvRechargeHistory.adapter =
                                    rechargeHistoryAdapter
                                rechargeHistoryActivity!!.tvNoData.visibility = View.GONE
                                rechargeHistoryActivity!!.rvRechargeHistory.visibility =
                                    View.VISIBLE
                            } else {
                                rechargeHistoryActivity!!.tvNoData.visibility = View.VISIBLE
                                rechargeHistoryActivity!!.rvRechargeHistory.visibility = View.GONE
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

                override fun onFailure(p0: Call<RechargeHistoryModel?>, throwable: Throwable) {
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

    private fun marginApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).marginDetail()
            call1?.enqueue(object : Callback<MarginModel?> {
                override fun onResponse(
                    call: Call<MarginModel?>, response: Response<MarginModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val marginModel: MarginModel? = response.body()
                        if (marginModel != null) {
                            if (marginModel.results?.operatorCommission?.isNotEmpty() == true) {
                                marginsAdapter = MarginsAdapter(
                                    marginModel.results?.operatorCommission!!,
                                    mContext!!
                                )
                                rechargeHistoryActivity!!.rvRechargeHistory.adapter = marginsAdapter
                                rechargeHistoryActivity!!.tvNoData.visibility = View.GONE
                                rechargeHistoryActivity!!.rvRechargeHistory.visibility =
                                    View.VISIBLE
                            } else {
                                rechargeHistoryActivity!!.tvNoData.visibility = View.VISIBLE
                                rechargeHistoryActivity!!.rvRechargeHistory.visibility = View.GONE
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

                override fun onFailure(p0: Call<MarginModel?>, throwable: Throwable) {
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