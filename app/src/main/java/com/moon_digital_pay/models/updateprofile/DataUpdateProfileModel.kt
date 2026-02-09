package com.moon_digital_pay.models.updateprofile

import com.google.gson.annotations.SerializedName

@Suppress("unused")
class DataUpdateProfileModel {
    @SerializedName("aadhaar_verify_status")
    var aadhaarVerifyStatus: String? = null

    @SerializedName("aadhar_number")
    var aadharNumber: Any? = null

    @SerializedName("access_token")
    var accessToken: Any? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("amount_limit")
    var amountLimit: String? = null

    @SerializedName("commission_type")
    var commissionType: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("created_by")
    var createdBy: Any? = null

    @SerializedName("device_token")
    var deviceToken: Any? = null

    @SerializedName("device_type")
    var deviceType: String? = null

    @SerializedName("distributor_id")
    var distributorId: Any? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("email_token")
    var emailToken: Any? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("full_name")
    var fullName: String? = null

    @SerializedName("id")
    var id: Long? = null

    @SerializedName("image_url")
    var imageUrl: String? = null

    @SerializedName("joinning_bonus_add")
    var joinningBonusAdd: String? = null

    @SerializedName("last_name")
    var lastName: Any? = null

    @SerializedName("latitude")
    var latitude: String? = null

    @SerializedName("login_count")
    var loginCount: String? = null

    @SerializedName("longitude")
    var longitude: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("mobile_token")
    var mobileToken: Any? = null

    @SerializedName("mobile_verified_at")
    var mobileVerifiedAt: Any? = null

    @SerializedName("mobile_verify_status")
    var mobileVerifyStatus: String? = null

    @SerializedName("mpin")
    var mpin: Any? = null

    @SerializedName("mpin_otp")
    var mpinOtp: Any? = null

    @SerializedName("notification_status")
    var notificationStatus: String? = null

    @SerializedName("package_id")
    var packageId: Any? = null

    @SerializedName("permissions_data")
    var permissionsData: Any? = null

    @SerializedName("pincode")
    var pincode: String? = null

    @SerializedName("profile_image")
    var profileImage: String? = null

    @SerializedName("profile_status")
    var profileStatus: String? = null

    @SerializedName("redeem_refer_amount")
    var redeemReferAmount: String? = null

    @SerializedName("refer_code")
    var referCode: Any? = null

    @SerializedName("referral_earnings")
    var referralEarnings: String? = null

    @SerializedName("role")
    var role: String? = null

    @SerializedName("secure_key")
    var secureKey: Any? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("super_dist_id")
    var superDistId: Any? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("upi_id")
    var upiId: Any? = null

    @SerializedName("wallet")
    var wallet: String? = null
}
