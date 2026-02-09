package com.moon_digital_pay.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moon_digital_pay.adapter.PlanDetailsAdapter
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.FragmentPlanItemListBinding
import com.moon_digital_pay.models.checkrechargeplan.ResultsCheckRechargePlanModel
import com.moon_digital_pay.utils.BaseFragment
import com.moon_digital_pay.utils.interfacef.HomeClickInterface

class PlanItemListFragment : BaseFragment() {
    private var viewBinding: FragmentPlanItemListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPlanItemListBinding.inflate(inflater, container, false)
        val model =
            arguments?.getSerializable(Constants.modelIntent) as ResultsCheckRechargePlanModel

        val planDetailsAdapter = mContext?.let {
            model.plans?.let { it1 ->
                PlanDetailsAdapter(it1, it, object : HomeClickInterface {
                    override fun homeClick(position: Int?) {
                        val selectPlan = model.plans?.get(position ?: 0)
                        val intent = Intent()
                        intent.putExtra(Constants.modelIntent, selectPlan)
                        mContext?.setResult(RESULT_OK, intent)
                        mContext?.onBackPressed()
                    }

                })
            }
        }
        viewBinding?.rvProductList?.adapter = planDetailsAdapter
        return viewBinding?.root
    }
}