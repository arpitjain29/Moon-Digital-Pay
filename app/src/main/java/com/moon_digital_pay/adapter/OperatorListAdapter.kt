package com.moon_digital_pay.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.moon_digital_pay.R
import com.moon_digital_pay.databinding.HomeLayoutBinding
import com.moon_digital_pay.models.operatorlist.DataOperatorListModel
import com.moon_digital_pay.utils.CommonFunction
import com.moon_digital_pay.utils.interfacef.OperatorClickInterface

class OperatorListAdapter(
    private val dataOperatorListModel: List<DataOperatorListModel>,
    private val context: Activity,
    private val homeClickInterface: OperatorClickInterface
) : RecyclerView.Adapter<OperatorListAdapter.ItemViewHolder>() {

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

        holder.layoutBinding.tvHomeLayout.text = dataOperatorListModel[position].name
        dataOperatorListModel[position].imageUrl?.let {
            CommonFunction.loadImageViaGlide(
                context, it, holder.layoutBinding.ivHomeLayout, R.drawable.ic_placeholder_image
            )
        }
        holder.layoutBinding.llHomeLayout.setOnClickListener {
            homeClickInterface.operatorClick(dataOperatorListModel[position])
        }
    }

    override fun getItemCount(): Int {
        return dataOperatorListModel.size
    }

    class ItemViewHolder(val layoutBinding: HomeLayoutBinding) :
        RecyclerView.ViewHolder(layoutBinding.root)
}