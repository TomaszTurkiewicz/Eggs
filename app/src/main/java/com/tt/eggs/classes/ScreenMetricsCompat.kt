package com.tt.eggs.classes

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi

object ScreenMetricsCompat {

    private val api: Api = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ApiLevel30() else Api()

    fun getScreenSize(context: Context): Int {
        val size = api.getScreenSize(context)
        val width = size.width / 20
        val height = size.height / 10
        return if (width > height) height else width
    }
    @Suppress("DEPRECATION")
    private open class Api{
        open fun getScreenSize(context: Context) : Size {
            val display = context.getSystemService(WindowManager::class.java).defaultDisplay
            val metrics = if(display!=null){
                DisplayMetrics().also { display.getRealMetrics(it) }
            } else {
                Resources.getSystem().displayMetrics
            }
            return Size(metrics.widthPixels, metrics.heightPixels)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private class ApiLevel30 : Api() {
        override fun getScreenSize(context: Context): Size {
            val metrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
            return Size(metrics.bounds.width(), metrics.bounds.height())
        }
    }
}