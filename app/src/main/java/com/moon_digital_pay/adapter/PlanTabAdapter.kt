package com.moon_digital_pay.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.fragment.PlanItemListFragment
import com.moon_digital_pay.models.checkrechargeplan.ResultsCheckRechargePlanModel

class PlanTabAdapter(fm: FragmentManager?, private var planItem : List<ResultsCheckRechargePlanModel>) :
    FragmentPagerAdapter(
        fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    var fragment: Fragment? = null
    override fun getItem(position: Int): Fragment {
        for (i in planItem.indices) {
            if (i == position) {
                val args = Bundle()
                args.putSerializable(Constants.modelIntent, planItem[position])
                 fragment = PlanItemListFragment()
                fragment!!.arguments = args
            }
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return planItem.size
    }

    override fun getItemPosition(`object`: Any): Int {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE
    }
}