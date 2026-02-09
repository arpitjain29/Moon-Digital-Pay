package com.moon_digital_pay.apiUtils

import com.moon_digital_pay.models.CommonModel
import com.moon_digital_pay.models.checkBalance.CheckBalanceModel
import com.moon_digital_pay.models.checkRechargeROfferPlan.CheckRechargeROfferPlanModel
import com.moon_digital_pay.models.checkoperator.CheckOperatorModel
import com.moon_digital_pay.models.checkrechargeplan.CheckRechargePlanModel
import com.moon_digital_pay.models.dashboard.DashboardModel
import com.moon_digital_pay.models.earningreport.EarningReportModel
import com.moon_digital_pay.models.forgotpassword.ForgotPasswordModel
import com.moon_digital_pay.models.getprofile.GetProfileModel
import com.moon_digital_pay.models.getwallet.GetWalletModel
import com.moon_digital_pay.models.login.LoginModel
import com.moon_digital_pay.models.margin.MarginModel
import com.moon_digital_pay.models.operatorlist.OperatorListModel
import com.moon_digital_pay.models.otp.OtpModel
import com.moon_digital_pay.models.paymentrequest.PaymentRequestModel
import com.moon_digital_pay.models.rechargehistory.RechargeHistoryModel
import com.moon_digital_pay.models.referearn.ReferEarnModel
import com.moon_digital_pay.models.regions.RegionsListModel
import com.moon_digital_pay.models.updateprofile.UpdateProfileModel
import com.moon_digital_pay.parameters.ChangePasswordParams
import com.moon_digital_pay.parameters.CheckOperatorParams
import com.moon_digital_pay.parameters.CheckRechargePlanParams
import com.moon_digital_pay.parameters.CheckRechargeROfferPlanParams
import com.moon_digital_pay.parameters.ForgotPasswordParams
import com.moon_digital_pay.parameters.LoginParams
import com.moon_digital_pay.parameters.MobileRechargeParams
import com.moon_digital_pay.parameters.OtpParams
import com.moon_digital_pay.parameters.RechargeHistoryParams
import com.moon_digital_pay.parameters.ResetPasswordParams
import com.moon_digital_pay.parameters.SignupParams
import com.moon_digital_pay.parameters.UpdateProfileParams
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query


interface ApiInterface {

    @POST(ApiUrlEndpoint.LOGIN_API)
    fun loginUserApi(@Body loginParams: LoginParams?): Call<LoginModel?>?

    @POST(ApiUrlEndpoint.SIGNUP_API)
    fun signupUserApi(@Body signupParams: SignupParams?): Call<LoginModel?>?

    @POST(ApiUrlEndpoint.OTP_API)
    fun otpApi(@Body forgotPasswordParams: ForgotPasswordParams?): Call<OtpModel?>?

    @POST(ApiUrlEndpoint.VERIFY_OTP_API)
    fun verifyOtpApi(@Body otpParams: OtpParams?): Call<LoginModel?>?

    @POST(ApiUrlEndpoint.RESEND_OTP_API)
    fun resendOtpApi(@Body forgotPasswordParams: ForgotPasswordParams?): Call<CommonModel?>?

    @GET(ApiUrlEndpoint.CHECK_BALANCE_API)
    fun checkBalanceApi(): Call<CheckBalanceModel?>?

    @GET(ApiUrlEndpoint.DASHBOARD_API)
    fun dashboardApi(): Call<DashboardModel?>?

    @GET(ApiUrlEndpoint.OPERATOR_LIST_API)
    fun operatorListApi(@Query("operator_type") operatorType: String): Call<OperatorListModel?>?

    @POST(ApiUrlEndpoint.MOBILE_RECHARGE_API)
    fun mobileRechargeApi(@Body mobileRechargeParams: MobileRechargeParams): Call<CommonModel>?

    @POST(ApiUrlEndpoint.CHECK_RECHARGE_PLAN_API)
    fun checkRechargePlanApi(@Body checkRechargePlanParams: CheckRechargePlanParams): Call<CheckRechargePlanModel?>?

    @POST(ApiUrlEndpoint.CHECK_OPERATOR_API)
    fun checkOperatorApi(@Body checkOperatorParams: CheckOperatorParams): Call<CheckOperatorModel?>?

    @GET(ApiUrlEndpoint.Regions_API)
    fun regionsApi(): Call<RegionsListModel?>?

    @GET(ApiUrlEndpoint.PROFILE_API)
    fun profileApi(): Call<GetProfileModel?>?

    @POST(ApiUrlEndpoint.PROFILE_API)
    fun updateProfileApi(@Body updateProfileParams: UpdateProfileParams): Call<UpdateProfileModel?>?

    @POST(ApiUrlEndpoint.CHECK_RECHARGE_R_OFFER_PLAN_API)
    fun checkRechargeROfferPlanApi(@Body checkRechargeROfferPlanParams: CheckRechargeROfferPlanParams): Call<CheckRechargeROfferPlanModel?>?

    @POST(ApiUrlEndpoint.RECHARGE_HISTORY_API)
    fun rechargeHistoryApi(@Body rechargeHistoryParams: RechargeHistoryParams): Call<RechargeHistoryModel?>?

    @GET(ApiUrlEndpoint.GET_WALLET_API)
    fun getWallet(): Call<GetWalletModel?>?

    @POST(ApiUrlEndpoint.FORGET_PASSWORD_API)
    fun forgotPassword(@Body forgotPasswordParams: ForgotPasswordParams): Call<ForgotPasswordModel?>?

    @POST(ApiUrlEndpoint.RESET_PASSWORD_API)
    fun resetPassword(@Body resetPasswordParams: ResetPasswordParams): Call<CommonModel?>?

    @POST(ApiUrlEndpoint.PAYMENT_REQUEST_API)
    @Multipart
    fun paymentReqApi(
        @Part profilePic: MultipartBody.Part?,
        @PartMap partMap: MutableMap<String, RequestBody>,
        ): Call<PaymentRequestModel?>?

    @GET(ApiUrlEndpoint.COMMISSION_DETAILS_API)
    fun marginDetail(): Call<MarginModel?>?

    @POST(ApiUrlEndpoint.CHANGE_PASSWORD_API)
    fun changePasswordApi(@Body changePasswordParams: ChangePasswordParams): Call<CommonModel?>?

    @GET(ApiUrlEndpoint.EARNING_REPORT_API)
    fun earningReport(): Call<EarningReportModel?>?

    @GET(ApiUrlEndpoint.GET_REFERRAL_DETAILS_API)
    fun getReferralApi(): Call<ReferEarnModel?>?
}