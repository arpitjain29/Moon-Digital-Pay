package com.moon_digital_pay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.moon_digital_pay.R
import com.moon_digital_pay.models.dashboard.BannersDashboardModel
import com.moon_digital_pay.utils.CommonFunction

class SliderAdapterExample(
    private val context: Context,
    private val mBannerItems: MutableList<BannersDashboardModel>,
    isInfinite: Boolean
) : LoopingPagerAdapter<BannersDashboardModel>(mBannerItems, isInfinite) {


    override fun getCount(): Int {
        return mBannerItems.size
    }


    override fun inflateView(
        viewType: Int, container: ViewGroup, listPosition: Int
    ): View {
        return LayoutInflater.from(context).inflate(R.layout.banner_item_layout, container, false)
    }

    override fun bindView(
        convertView: View, listPosition: Int, viewType: Int
    ) {
        if (listPosition >= 0 && listPosition < mBannerItems.size) {
            val sliderItem = mBannerItems[listPosition]
            val imageView: ImageView = convertView.findViewById(R.id.imageView)
            sliderItem.imageUrl?.let {
                CommonFunction.loadImageViaGlide(
                    context,
                    it, imageView, R.drawable.ic_app_logo
                )
            }
        }
    }
}