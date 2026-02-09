package com.moon_digital_pay.activitys

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.moon_digital_pay.R
import com.moon_digital_pay.adapter.PlanTabAdapter
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityPlanDetailsBinding
import com.moon_digital_pay.models.checkrechargeplan.CheckRechargePlanModel
import com.moon_digital_pay.parameters.CheckRechargePlanParams
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class PlanDetailsActivity : BaseActivity() {

    private var planDetailsBinding: ActivityPlanDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        planDetailsBinding = ActivityPlanDetailsBinding.inflate(layoutInflater)
        setContentView(planDetailsBinding!!.root)

        planDetailsBinding!!.llAppbarLayout.ivBackAppbar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        planDetailsBinding!!.llAppbarLayout.tvTittleAppbar.text =
            resources.getString(R.string.plan_details)

        planDetailsBinding?.tabLayout?.visibility = View.GONE
        planDetailsBinding?.viewPager?.visibility = View.GONE
        planDetailsBinding?.tvNotFound?.visibility = View.GONE
        val checkRechargePlanParams = CheckRechargePlanParams()
        checkRechargePlanParams.operatorId = intent.getIntExtra(Constants.idIntent, 0)
        checkRechargePlanParams.operatorType = Constants.mobile
        checkRechargePlanParams.operatorCircle = "mp"
        checkRechargePlanApi(checkRechargePlanParams)

    }

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
                        if (checkRechargePlanModel?.status == 200) {
                            if (checkRechargePlanModel.results?.isNotEmpty() == true) {
                                planDetailsBinding?.tabLayout?.visibility = View.VISIBLE
                                planDetailsBinding?.viewPager?.visibility = View.VISIBLE
                                planDetailsBinding?.tvNotFound?.visibility = View.GONE
                                for (i in checkRechargePlanModel.results?.indices!!) {
                                    planDetailsBinding!!.tabLayout.addTab(
                                        planDetailsBinding!!.tabLayout.newTab()
                                            .setText(checkRechargePlanModel.results?.get(i)?.name)
                                    )
                                }

                                val adapter = checkRechargePlanModel.results?.let {
                                    PlanTabAdapter(
                                        supportFragmentManager, it
                                    )
                                }
                                planDetailsBinding?.viewPager?.adapter = adapter
                                planDetailsBinding?.viewPager?.addOnPageChangeListener(
                                    TabLayoutOnPageChangeListener(
                                        planDetailsBinding?.tabLayout
                                    )
                                )

                                planDetailsBinding?.tabLayout?.addOnTabSelectedListener(object :
                                    OnTabSelectedListener {
                                    override fun onTabSelected(tab: TabLayout.Tab) {
                                        planDetailsBinding?.viewPager?.currentItem = tab.position
                                    }

                                    override fun onTabUnselected(tab: TabLayout.Tab) {}
                                    override fun onTabReselected(tab: TabLayout.Tab) {}
                                })
                            } else {
                    planDetailsBinding?.tabLayout?.visibility = View.GONE
                    planDetailsBinding?.viewPager?.visibility = View.GONE
                    planDetailsBinding?.tvNotFound?.visibility = View.VISIBLE
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
}