package com.moon_digital_pay.activitys

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.moon_digital_pay.R
import com.moon_digital_pay.adapter.WalletHistoryAdapter
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.databinding.ActivityWalletHistoryBinding
import com.moon_digital_pay.models.getwallet.DatumGetWalletModel
import com.moon_digital_pay.models.getwallet.GetWalletModel
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.WalletHistoryInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WalletHistoryActivity : BaseActivity() {

    private var walletHistoryBinding: ActivityWalletHistoryBinding? = null
    private var walletHistoryAdapter: WalletHistoryAdapter? = null
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walletHistoryBinding = ActivityWalletHistoryBinding.inflate(layoutInflater)
        setContentView(walletHistoryBinding!!.root)

        walletHistoryBinding!!.ivBackWalletHistory.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        walletHistoryBinding!!.llStartDate.setOnClickListener {
            showDatePicker("1")
        }

        walletHistoryBinding!!.llEndStart.setOnClickListener {
            showDatePicker("2")
        }

        walletDataApi()
    }

    private fun showDatePicker(valueDate: String) {
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                if (valueDate == "1") {
                    walletHistoryBinding!!.tvStartDate.text = formattedDate
                } else {
                    walletHistoryBinding!!.tvEndDate.text = formattedDate
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun walletDataApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).getWallet()
            call1?.enqueue(object : Callback<GetWalletModel?> {
                override fun onResponse(
                    call: Call<GetWalletModel?>, response: Response<GetWalletModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val getWalletModel: GetWalletModel? = response.body()
                        if (getWalletModel != null) {
                            walletHistoryBinding!!.tvAmountWalletHistory.text =
                                String.format(
                                    "%s %s", resources.getString(R.string.rs),
                                    getWalletModel.data?.wallet
                                )
                            if (getWalletModel.data?.transaction?.data?.isNotEmpty() == true){
                                walletHistoryAdapter = getWalletModel.data?.transaction?.data?.let {
                                    WalletHistoryAdapter(
                                        it, mContext!!,object :WalletHistoryInterface{
                                            override fun walletHistoryClick(
                                                data: DatumGetWalletModel,
                                                position: Int?
                                            ) {
                                                val intent = Intent(
                                                    mContext,
                                                    HistoryActivity::class.java)
                                                intent.putExtra("listWalletHistory", data)
                                                intent.putExtra("screenIntent","wallet")
                                                startActivity(intent)
                                            }
                                        }
                                    )
                                }
                                walletHistoryBinding!!.rvWalletHistory.adapter = walletHistoryAdapter
                                walletHistoryBinding!!.tvNoData.visibility = View.GONE
                                walletHistoryBinding!!.rvWalletHistory.visibility = View.VISIBLE
                            }else{
                                walletHistoryBinding!!.tvNoData.visibility = View.VISIBLE
                                walletHistoryBinding!!.rvWalletHistory.visibility = View.GONE
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

                override fun onFailure(p0: Call<GetWalletModel?>, throwable: Throwable) {
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