package com.moon_digital_pay.models.login

import com.google.gson.annotations.SerializedName

class UserLoginModel {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("role")
    var role: String? = null
    @SerializedName("first_name")
    var firstName: String? = null
    @SerializedName("last_name")
    var lastName: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("mobile_verify_status")
    var mobileVerifyStatus: String? = null
    @SerializedName("profile_image")
    var profileImage: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("latitude")
    var latitude: String? = null
    @SerializedName("longitude")
    var longitude: String? = null
    @SerializedName("aadhar_number")
    var aadharNumber: String? = null
    @SerializedName("aadhaar_verify_status")
    var aadhaarVerifyStatus: String? = null
    @SerializedName("wallet")
    var wallet: String? = null
    @SerializedName("commission_type")
    var commissionType: String? = null
    @SerializedName("package_id")
    var packageId: String? = null
    @SerializedName("mobile_verified_at")
    var mobileVerifiedAt: String? = null
    @SerializedName("mobile_token")
    var mobileToken: String? = null
    @SerializedName("login_count")
    var loginCount: String? = null
    @SerializedName("access_token")
    var accessToken: String? = null
    @SerializedName("device_type")
    var deviceType: String? = null
    @SerializedName("device_token")
    var deviceToken: String? = null
    @SerializedName("refer_code")
    var referCode: String? = null
    @SerializedName("referral_earnings")
    var referralEarnings: String? = null
    @SerializedName("redeem_refer_amount")
    var redeemReferAmount: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("upi_id")
    var upiId: String? = null
    @SerializedName("amount_limit")
    var amountLimit: String? = null
    @SerializedName("secure_key")
    var secureKey: String? = null
    @SerializedName("created_by")
    var createdBy: String? = null
    @SerializedName("super_dist_id")
    var superDistId: String? = null
    @SerializedName("distributor_id")
    var distributorId: String? = null
    @SerializedName("joinning_bonus_add")
    var joinningBonusAdd: String? = null
    @SerializedName("notification_status")
    var notificationStatus: String? = null
    @SerializedName("profile_status")
    var profileStatus: String? = null
    @SerializedName("email_token")
    var emailToken: String? = null
    @SerializedName("mpin_otp")
    var mpinOtp: String? = null
    @SerializedName("mpin")
    var mpin: String? = null
    @SerializedName("pincode")
    var pincode: String? = null
    @SerializedName("permissions_data")
    var permissionsData: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    @SerializedName("full_name")
    var fullName: String? = null
    @SerializedName("image_url")
    var imageUrl: String? = null
    @SerializedName("support_name")
    var supportName: String? = null
    @SerializedName("support_numner")
    var supportNumner: String? = null
}