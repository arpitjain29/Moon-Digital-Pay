package com.moon_digital_pay

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.moon_digital_pay.activitys.ChangePasswordActivity
import com.moon_digital_pay.activitys.MyProfileActivity
import com.moon_digital_pay.activitys.RechargeHistoryActivity
import com.moon_digital_pay.activitys.ReferEarnActivity
import com.moon_digital_pay.activitys.WalletHistoryActivity
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityHomeBinding
import com.moon_digital_pay.databinding.LogoutPopupBinding
import com.moon_digital_pay.fragment.HomeFragment
import com.moon_digital_pay.utils.AppController
import com.moon_digital_pay.utils.BaseActivity

class HomeActivity : BaseActivity() {

    private var homeBinding: ActivityHomeBinding? = null
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding!!.root)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, homeBinding!!.drawerLayout,
            R.string.nav_open, R.string.nav_close
        )
        homeBinding!!.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        homeBinding!!.navView.setNavigationItemSelectedListener(
            OnNavigationItemSelectedListener { menuItem ->
                val frag: Fragment? = null
                val itemId = menuItem.itemId
                when (itemId) {
                    R.id.dashboard -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                    }

                    R.id.my_profile -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                        startActivity(Intent(mContext, MyProfileActivity::class.java))
                    }

                    R.id.change_password -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                        startActivity(Intent(mContext, ChangePasswordActivity::class.java))
                    }

                    R.id.recharge_history -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                        startActivity(
                            Intent(mContext, RechargeHistoryActivity::class.java)
                                .putExtra(Constants.screenTypes, Constants.recharge)
                        )
                    }

                    R.id.wallet_history -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                        startActivity(Intent(mContext, WalletHistoryActivity::class.java))
                    }

                    R.id.contact_us -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                    }

                    R.id.set_pin -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                    }

                    R.id.refer_earn -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                        startActivity(Intent(mContext, ReferEarnActivity::class.java))
                    }

                    R.id.share -> {
                        homeBinding!!.drawerLayout.closeDrawers()
                        val appPackageName: String = packageName
                        val sendIntent = Intent()
                        sendIntent.setAction(Intent.ACTION_SEND)
                        sendIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out the App at: https://play.google.com/store/apps/details?id=$appPackageName"
                        )
                        sendIntent.setType("text/plain")
                        startActivity(sendIntent)
                    }
                }

                Toast.makeText(applicationContext, menuItem.title, Toast.LENGTH_SHORT).show()
                if (frag != null) {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.flFrameLayout,
                        frag
                    )
                    transaction.commit()
                    homeBinding!!.drawerLayout.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                false
            })

        loadFragment(HomeFragment())

        homeBinding!!.bottomSheetDashboard.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_menu -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.nav_news -> {
                    comingSoonDialog()
//                    loadFragment(NewsFragment())
                    true
                }

                R.id.nav_setting -> {
                    comingSoonDialog()
//                    loadFragment(SettingsFragment())
                    true
                }

                R.id.nav_logout -> {
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
                    dialog.setCancelable(true)
                    val binding: LogoutPopupBinding = LogoutPopupBinding.inflate(
                        LayoutInflater.from(
                            mContext
                        ), null, false
                    )
                    dialog.setContentView(binding.root)
                    binding.tvMessageTextPopup.text =
                        resources.getString(R.string.are_you_sure_you_want_to_logout)
                    binding.tvNoTextPopup.setOnClickListener {
                        dialog.dismiss()
                    }
                    binding.tvYesTextPopup.setOnClickListener {
                        dialog.dismiss()
                        AppController.instance?.sessionManager?.logoutUser()
                    }
                    dialog.show()
                    true
                }
                else -> false
            }
        }
        updateProfileMenu()
    }

    private fun updateProfileMenu() {
        val headerView = homeBinding?.navView?.getHeaderView(0)
        val profileImageView = headerView?.findViewById<ImageView>(R.id.ivUserImageHeader)
        val usernameTextView = headerView?.findViewById<TextView>(R.id.tvNameHomeHeader)
        val emailTextView = headerView?.findViewById<TextView>(R.id.tvMobileHomeHeader)
        usernameTextView?.text =
            AppController.instance?.sessionManager?.getLoginModel?.user?.fullName
        emailTextView?.text = AppController.instance?.sessionManager?.getLoginModel?.user?.mobile
    }

    override fun onResume() {
        super.onResume()
        updateProfileMenu()
    }

    fun openDrawer() {
        homeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFrameLayout, fragment)
        transaction.commit()
    }

    private fun comingSoonDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Coming soon...")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        if (homeBinding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}