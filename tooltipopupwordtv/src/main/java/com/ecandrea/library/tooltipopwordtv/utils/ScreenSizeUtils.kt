package com.ecandrea.library.tooltipopwordtv.utils

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager


object ScreenSizeUtils {

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getLocationOnScreen(v: View, context: Context): Point {
        val position = IntArray(2)
        v.getLocationOnScreen(position)

        return Point(position[0], position[1] - getStatusBarHeight(context))
    }

    fun getWidthWindow(context: Context): Point {
        val size = Point()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        display.getSize(size)
        return size
    }

}