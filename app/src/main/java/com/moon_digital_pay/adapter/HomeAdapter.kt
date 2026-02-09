package com.moon_digital_pay.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.moon_digital_pay.R
import com.moon_digital_pay.databinding.HomeLayoutBinding
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.HomeClickInterface

class HomeAdapter(
    private val mList: List<String>,
    private val context: Activity,
    private val homeClickInterface: HomeClickInterface
) : RecyclerView.Adapter<HomeAdapter.ItemViewHolder>() {

    private var imageArrayLists = arrayOf(
        R.drawable.ic_mobile_recharge,
        R.drawable.ic_tv_recharge,
        R.drawable.ic_my_profile,
//        R.drawable.ic_recharge_history,
//        R.drawable.ic_load_wallet,
        R.drawable.ic_wallet,
//        R.drawable.ic_change_password,
        R.drawable.ic_complaints,
        R.drawable.ic_supports
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            HomeLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val displayMetrics = CommonFunction.screenSize(context)
        val width = (displayMetrics.widthPixels / 3.3).toInt()
        val layoutParams = LinearLayout.LayoutParams(
            width, width
        )

        layoutParams.setMargins(20, 10, 10, 10)
        holder.layoutBinding.llHomeLayout.layoutParams = layoutParams

        holder.layoutBinding.tvHomeLayout.text = mList[position]
        holder.layoutBinding.ivHomeLayout.setImageResource(imageArrayLists[position])
        holder.layoutBinding.llHomeLayout.setOnClickListener {
            homeClickInterface.homeClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ItemViewHolder(val layoutBinding: HomeLayoutBinding) :
        RecyclerView.ViewHolder(layoutBinding.root)
}