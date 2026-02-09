package com.moon_digital_pay.fragment

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.moon_digital_pay.HomeActivity
import com.moon_digital_pay.R
import com.moon_digital_pay.activitys.AddWalletActivity
import com.moon_digital_pay.activitys.DthRechargeActivity
import com.moon_digital_pay.activitys.EarningReportActivity
import com.moon_digital_pay.activitys.MobileRechargeActivity
import com.moon_digital_pay.activitys.MyProfileActivity
import com.moon_digital_pay.activitys.RechargeHistoryActivity
import com.moon_digital_pay.activitys.WalletHistoryActivity
import com.moon_digital_pay.adapter.HomeAdapter
import com.moon_digital_pay.adapter.SliderAdapterExample
import com.moon_digital_pay.apiUtils.ApiClient
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.FragmentHomeBinding
import com.moon_digital_pay.models.checkBalance.CheckBalanceModel
import com.moon_digital_pay.models.dashboard.DashboardModel
import com.moon_digital_pay.models.getprofile.GetProfileModel
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseFragment
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.HomeClickInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class HomeFragment : BaseFragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var stringList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(context, 3)
        homeBinding.rvHome.layoutManager = layoutManager
        stringList = ArrayList()
        resources.getStringArray(R.array.text_array_list).forEach { unit ->
            stringList.add(unit)
        }
        val adapter = mContext?.let {
            HomeAdapter(stringList, it, object : HomeClickInterface {
                override fun homeClick(position: Int?) {
                    when (position) {
                        0 -> {
                            startActivity(Intent(mContext, MobileRechargeActivity::class.java))
                        }

                        1 -> {
                            startActivity(Intent(mContext, DthRechargeActivity::class.java))
                        }

                        2 -> {
                            startActivity(Intent(mContext, MyProfileActivity::class.java))
                        }

                        3 -> {
                            startActivity(Intent(mContext, WalletHistoryActivity::class.java))
                        }
                    }
                }
            })
        }
        homeBinding.rvHome.adapter = adapter

        homeBinding.ivUserProfile.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }

        homeBinding.ivRefreshAmount.setOnClickListener {
            dashboardCheckBalanceApi()
        }

        dashboardCheckBalanceApi()

        dashboardApi()
        homeBinding.tvNameHome.text =
            AppController.instance?.sessionManager?.getLoginModel?.user?.fullName
        homeBinding.tvMobileHome.text =
            AppController.instance?.sessionManager?.getLoginModel?.user?.mobile

        homeBinding.llAddMoney.setOnClickListener {
            startActivity(Intent(mContext, AddWalletActivity::class.java))
        }

        homeBinding.llRechargeHistory.setOnClickListener {
            startActivity(
                Intent(mContext, RechargeHistoryActivity::class.java)
                    .putExtra(Constants.screenTypes, Constants.recharge)
            )
        }
        homeBinding.llMyEarnings.setOnClickListener {
            startActivity(
                Intent(mContext, EarningReportActivity::class.java))
        }

        homeBinding.llMargins.setOnClickListener {
            startActivity(
                Intent(mContext, RechargeHistoryActivity::class.java)
                    .putExtra(Constants.screenTypes, Constants.margins)
            )
        }

        return homeBinding.root
    }



    override fun onResume() {
        super.onResume()
        homeBinding.tvNameHome.text =
            AppController.instance?.sessionManager?.getLoginModel?.user?.fullName
        homeBinding.tvMobileHome.text =
            AppController.instance?.sessionManager?.getLoginModel?.user?.mobile
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
                                homeBinding.tvWalletBalance.text = String.format(
                                    "%s %s",
                                    getString(R.string.rs),
                                    (checkBalanceModel.results?.wallet ?: 0)
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

    private fun dashboardApi() {
        if (isConnectingToInternet(mContext!!)) {
            showProgressDialog()
            val call1 = ApiClient.buildService(mContext).dashboardApi()
            call1?.enqueue(object : Callback<DashboardModel?> {
                override fun onResponse(
                    call: Call<DashboardModel?>, response: Response<DashboardModel?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val dashboardModel: DashboardModel? = response.body()
                        if (dashboardModel != null) {
                            if (dashboardModel.status == 200) {
                                val adapter = dashboardModel.data?.banners?.let {
                                    SliderAdapterExample(
                                        mContext!!, it, true
                                    )
                                }
                                homeBinding.loopingViewPager.adapter = adapter
                                homeBinding.pageIndicatorView.setViewPager(homeBinding.loopingViewPager)
                                homeBinding.pageIndicatorView.dataSetObserver?.let {
                                    homeBinding.loopingViewPager.adapter?.registerDataSetObserver(
                                        it
                                    )
                                }
                                homeBinding.tvCheckBalance.text = String.format(
                                    "%s %s",
                                    getString(R.string.rs),
                                    (dashboardModel.data?.walle ?: 0)
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
                                AppController.instance?.sessionManager?.logoutUser()

                            }
                        }
                    }

                }

                override fun onFailure(p0: Call<DashboardModel?>, throwable: Throwable) {
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