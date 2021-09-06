package com.example.watchstoreapp.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import androidx.fragment.app.FragmentActivity

class Constant {

    companion object{
        const val CATEGORY_TABEL = "productCategory"
        const val PRODUCT_TABEL = "products"
        const val CART_TABLE = "cart"
        const val USER_TABEL = "users"
        const val MyPREFERENCES = "store_preferences"

        fun getUsableHeight(activity:FragmentActivity):Int{
            val displayMetrics = DisplayMetrics()
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            val finalHeight = height - getNavigationBarHeight(activity)-getDeviceNavigtionHeight(activity)
            return finalHeight
        }
        private fun getNavigationBarHeight(activity:FragmentActivity): Int {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val metrics = DisplayMetrics()
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics)
                val usableHeight = metrics.heightPixels
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics)
                val realHeight = metrics.heightPixels
                return if (realHeight > usableHeight) realHeight - usableHeight else 0
            }
            return 0
        }

        private fun getDeviceNavigtionHeight(activity:FragmentActivity):Int{
            val resources: Resources = activity!!.resources
            val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                resources.getDimensionPixelSize(resourceId)-30
            } else 0
        }
    }
}