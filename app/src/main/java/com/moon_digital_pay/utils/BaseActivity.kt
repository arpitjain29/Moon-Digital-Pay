package com.moon_digital_pay.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.SelectFileLayoutBinding
import com.moon_digital_pay.utils.interfacef.LocationInterface
import com.moon_digital_pay.utils.interfacef.UploadImageInterface
import com.moon_digital_pay.utils.loader.ArcConfiguration
import com.moon_digital_pay.utils.loader.SimpleArcDialog
import java.io.ByteArrayOutputStream
import com.yalantis.ucrop.UCrop
import java.io.File

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener, UploadImageInterface {
    var mContext: Activity? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationInterface: LocationInterface? = null
    private var isCameraClick: Boolean = false
    private var REQUEST_CODE = Constants.PICK_IMAGE_CAMERA
    private var mUploadImageInterface: UploadImageInterface? = null

    fun isConnectingToInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    override fun onClick(p0: View?) {
        CommonFunction.hideKeyboardFrom(mContext!!, p0!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        mContext = this
        mUploadImageInterface = this
    }

    var mDialog: SimpleArcDialog? = null

    open fun showProgressDialog() {
        if (mDialog == null) {
            mDialog = SimpleArcDialog(mContext)
        }
        mDialog!!.setConfiguration(ArcConfiguration(mContext))
        mDialog!!.setCancelable(false)
        mDialog!!.show()
    }

    open fun hideProgressDialog() {
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
    }

    fun initLiveLocation(locationInterface: LocationInterface?) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        // method to get the location
//        // method to get the location
        this.locationInterface = locationInterface
        getLastLocation()
//        this.locationInterface = locationInterface
//        val location = Location("London")
//        location.latitude = 51.5072
//        location.longitude = 0.1276
//        this.locationInterface!!.fetchLocation(location)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        // check if permissions are given
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                // getting last location from FusedLocationClient object
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    task.result
                    requestNewLocationData()
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,request for permissions
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // Initializing LocationRequest object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            if (locationInterface != null) {
                locationInterface!!.fetchLocation(mLastLocation)
                locationInterface = null
            }
        }
    }


    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)

    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Constants.PERMISSION_LOCATION
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mFusedLocationClient != null) {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        try {
            if (requestCode == Constants.PERMISSIONS_REQUEST) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext!!, permissions[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        mContext!!,
                        permissions[1]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        mContext!!,
                        permissions[2]
                    )
                ) {
                } else if (ActivityCompat.checkSelfPermission(
                        mContext!!,
                        permissions[0]
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        mContext!!,
                        permissions[1]
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        mContext!!,
                        permissions[2]
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    //allowed
                    if (isCameraClick) {
                        takePhoto()
                    } else {
                        pickPhotoGallery()
                    }
                } else {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", mContext!!.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkUserRequestedDontAskAgain() {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            val rationalFalgCOARSE =
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            val rationalFalgFINE =
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            if (!rationalFalgCOARSE && !rationalFalgFINE) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    fun showPictureDialog() {
        val dialog = Dialog(mContext!!, R.style.CustomAlertDialogStylePopup)
        if (dialog.window != null) {
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }
        val binding: SelectFileLayoutBinding =
            SelectFileLayoutBinding.inflate(LayoutInflater.from(mContext), null, false)
        dialog.setContentView(binding.root)
        if (dialog.window != null) {
            dialog.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        binding.tvCameraSelectFile.setOnClickListener {
            isCameraClick = true
            permissionToStorage
            dialog.dismiss()
        }
        binding.tvGallerySelectFile.setOnClickListener {
            isCameraClick = false
            permissionToStorage
            dialog.dismiss()
        }
        binding.tvCancelSelectFile.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun takePhoto() {
        try {
            REQUEST_CODE = Constants.PICK_IMAGE_CAMERA
            val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(
                pictureIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("IntentReset")
    private fun pickPhotoGallery() {
        try {
            REQUEST_CODE = Constants.PICK_IMAGE_GALLERY
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(pickIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val permissionToStorage: Unit
        get() {
            if (SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                if ((ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.CAMERA
                    )
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    )
                            != PackageManager.PERMISSION_GRANTED)
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        ),
                        Constants.PERMISSIONS_REQUEST
                    )
                } else {
                    if (isCameraClick) {
                        takePhoto()
                    } else {
                        pickPhotoGallery()
                    }
                }
            } else if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if ((ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.CAMERA
                    )
                            != PackageManager.PERMISSION_GRANTED)
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.CAMERA
                        ),
                        Constants.PERMISSIONS_REQUEST
                    )
                } else {
                    if (isCameraClick) {
                        takePhoto()
                    } else {
                        pickPhotoGallery()
                    }
                }
            } else {
                if ((ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                        mContext!!,
                        Manifest.permission.CAMERA
                    )
                            != PackageManager.PERMISSION_GRANTED)
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), Constants.PERMISSIONS_REQUEST
                    )
                } else {
                    if (isCameraClick) {
                        takePhoto()
                    } else {
                        pickPhotoGallery()
                    }
                }
            }
        }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            mContext?.let { CommonFunction.hideKeyboardFrom(it, currentFocus!!) }

        }
        return super.dispatchTouchEvent(ev)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            var imageURI: Uri? = null
            if (result.resultCode == RESULT_OK) {
                if (REQUEST_CODE == Constants.PICK_IMAGE_CAMERA && result.data!!.extras != null) {
                    val photo = result.data!!.extras!!["data"] as Bitmap?
                    if (photo != null) {
                        imageURI = getImageUri(mContext!!, photo)
                    }
                    try {
                        if (imageURI != null) {
                            UCrop.of(imageURI, Uri.fromFile(File(mContext!!.cacheDir, ".jpg")))
                                .withAspectRatio(16F, 16F)
                                .start(mContext!!)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else if (REQUEST_CODE == Constants.PICK_IMAGE_GALLERY) {
                    try {
                        imageURI = result.data!!.data
                        if (imageURI != null) {
                            UCrop.of(imageURI, Uri.fromFile(File(mContext!!.cacheDir, ".jpg")))
                                .withAspectRatio(16F, 16F)
                                .start(mContext!!)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }

    open fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            val imageBitmap =
                CommonFunction.getRealPathFromGallery(
                    RealFileUtils.newInstance(mContext!!)
                    !!.getPath(resultUri!!)
                )
            mUploadImageInterface!!.onUploadImage(imageBitmap!!)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.printStackTrace()
        }
    }

    override fun onUploadImage(imageUrl: Bitmap) {

    }
}