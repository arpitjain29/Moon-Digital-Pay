package com.moon_digital_pay.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moon_digital_pay.R
import com.moon_digital_pay.databinding.FragmentSettingsBinding
import com.moon_digital_pay.utils.BaseFragment

class SettingsFragment : BaseFragment() {

    private lateinit var settingsBinding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        return settingsBinding.root
    }
}