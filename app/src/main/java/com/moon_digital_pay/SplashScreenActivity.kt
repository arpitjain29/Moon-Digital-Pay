package com.moon_digital_pay

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.moon_digital_pay.databinding.ActivitySplashScreenBinding
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity

class SplashScreenActivity : BaseActivity() {
    private var splashScreenBinding: ActivitySplashScreenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding!!.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler(Looper.getMainLooper()).postDelayed({
           AppController.instance?.sessionManager?.checkLogin()
        }, 3000)
    }
}