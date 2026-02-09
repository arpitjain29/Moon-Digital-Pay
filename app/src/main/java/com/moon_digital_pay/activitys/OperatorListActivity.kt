package com.moon_digital_pay.activitys

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.moon_digital_pay.R
import com.moon_digital_pay.adapter.OperatorListAdapter
import com.moon_digital_pay.adapter.RegionsAdapter
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityOperatorListBinding
import com.moon_digital_pay.models.operatorlist.DataOperatorListModel
import com.moon_digital_pay.models.operatorlist.OperatorListModel
import com.moon_digital_pay.models.regions.RegionsListModel
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.HomeClickInterface
import com.moon_digital_pay.utils.interfacef.OperatorClickInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class OperatorListActivity : BaseActivity() {

    private var operatorListBinding: ActivityOperatorListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        operatorListBinding = ActivityOperatorListBinding.inflate(layoutInflater)
        setContentView(operatorListBinding!!.root)

        operatorListBinding!!.llAppbarLayout.ivBackAppbar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        if (intent.getStringExtra(Constants.fromWhereIntent).equals(Constants.operatorIntent)) {
            operatorListBinding!!.llAppbarLayout.tvTittleAppbar.text =
                resources.getString(R.string.select_operator)
            val layoutManager = GridLayoutManager(mContext, 3)
            operatorListBinding!!.rvSelectOperator.layoutManager = layoutManager

            intent.getStringExtra(Constants.typeIntent)?.let { operatorListApi(it) }

            println("intent value ======= "+intent.getStringExtra(Constants.typeIntent))

        } else if (intent.getStringExtra(Constants.fromWhereIntent)
                .equals(Constants.circleIntent)
        ) {
            operatorListBinding!!.llAppbarLayout.tvTittleAppbar.text =
                resources.getString(R.string.select_circle)
            val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            operatorListBinding!!.rvSelectOperator.layoutManager = layoutManager

            regionsApi()
        }
    }

    private fun regionsApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).regionsApi()
            call1?.enqueue(object : Callback<RegionsListModel?> {
                override fun onResponse(
                    call: Call<RegionsListModel?>, response: Response<RegionsListModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val regionsListModel: RegionsListModel? = response.body()
                        val adapter = mContext?.let {
                            regionsListModel?.results?.let { it1 ->
                                RegionsAdapter(it1, it, object : HomeClickInterface {

                                    override fun homeClick(position: Int?) {
                                        val intent = Intent()
                                        intent.putExtra(Constants.modelIntent,
                                            position?.let { it2 ->
                                                regionsListModel.results?.get(
                                                    it2
                                                )
                                            })
                                        intent.putExtra(
                                            Constants.fromWhereIntent,
                                            getIntent().getStringExtra(Constants.fromWhereIntent)
                                        )
                                        setResult(RESULT_OK, intent)
                                        onBackPressedDispatcher.onBackPressed()
                                    }
                                })
                            }
                        }
                        operatorListBinding!!.rvSelectOperator.adapter = adapter

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

                override fun onFailure(p0: Call<RegionsListModel?>, throwable: Throwable) {
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

    private fun operatorListApi(typeOfOperator: String) {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).operatorListApi(typeOfOperator)
            call1?.enqueue(object : Callback<OperatorListModel?> {
                override fun onResponse(
                    call: Call<OperatorListModel?>, response: Response<OperatorListModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val operatorListModel: OperatorListModel? = response.body()
                        if (operatorListModel != null) {
                            val adapter = mContext?.let {
                                operatorListModel.data?.let { it1 ->
                                    OperatorListAdapter(it1, it, object : OperatorClickInterface {

                                        override fun operatorClick(dataOperatorListModel: DataOperatorListModel) {
                                            val intent = Intent()
                                            intent.putExtra(
                                                Constants.modelIntent, dataOperatorListModel
                                            )
                                            intent.putExtra(
                                                Constants.fromWhereIntent,
                                                getIntent().getStringExtra(Constants.fromWhereIntent)
                                            )
                                            setResult(RESULT_OK, intent)
                                            onBackPressedDispatcher.onBackPressed()
                                        }
                                    })
                                }
                            }
                            operatorListBinding!!.rvSelectOperator.adapter = adapter
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

                override fun onFailure(p0: Call<OperatorListModel?>, throwable: Throwable) {
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