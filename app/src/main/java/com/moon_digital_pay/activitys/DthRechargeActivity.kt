package com.moon_digital_pay.activitys

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.result.contract.ActivityResultContracts
import com.moon_digital_pay.R
import com.moon_digital_pay.apiUtils.Constants
import com.moon_digital_pay.databinding.ActivityDthRechargeBinding
import com.moon_digital_pay.utils.BaseActivity
import com.moon_digital_pay.utils.CommonFunction

class DthRechargeActivity : BaseActivity() {

    private var dthRechargeBinding: ActivityDthRechargeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dthRechargeBinding = ActivityDthRechargeBinding.inflate(layoutInflater)
        setContentView(dthRechargeBinding!!.root)

        dthRechargeBinding!!.ivBackTv.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        dthRechargeBinding!!.tvChangeOperator.setOnClickListener {
            someActivityResultLauncher.launch(
                Intent(mContext, OperatorListActivity::class.java)
                    .putExtra(Constants.typeIntent, Constants.dth).putExtra(
                        Constants.fromWhereIntent, Constants.operatorIntent))
        }
        dthRechargeBinding!!.etOperatorName.setOnClickListener {
            someActivityResultLauncher.launch(
                Intent(mContext, OperatorListActivity::class.java)
                    .putExtra(Constants.typeIntent, Constants.dth).putExtra(
                        Constants.fromWhereIntent, Constants.operatorIntent))
        }
    }

    private var someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                val operatorIds : String = data?.getStringExtra("operatorId").toString()
                val operatorNames : String = data?.getStringExtra("operatorName").toString()
                val operatorImages : String = data?.getStringExtra("operatorImage").toString()
                val Ids : String = data?.getStringExtra("Id").toString()
                val operatorTypes : String = data?.getStringExtra("operatorType").toString()
                println("operator name ================= "+operatorNames)
                dthRechargeBinding!!.etOperatorName.text = Editable.Factory.getInstance().newEditable(operatorNames)
                CommonFunction.loadImageViaGlide(mContext,operatorImages,dthRechargeBinding!!.ivOperatorImage,
                    R.drawable.ic_placeholder_image)
            }
        }
}