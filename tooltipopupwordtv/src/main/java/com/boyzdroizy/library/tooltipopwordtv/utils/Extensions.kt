package com.boyzdroizy.library.tooltipopwordtv

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.boyzdroizy.library.tooltipopwordtv.utils.TooltipPopupConstants

internal fun View.show() {
    this.visibility = View.VISIBLE
}

internal fun View.hide() {
    this.visibility = View.GONE
}

internal fun View.invisible() {
    this.visibility = View.INVISIBLE
}

internal fun View.isVisible(value: Boolean) {
    if (value) this.show()
    else this.hide()
}

internal val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

internal val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


internal fun Context.contextColor(resource: Int): Int {
    return ContextCompat.getColor(this, resource)
}


internal fun Context.contextDrawable(resource: Int): Drawable? {
    return ContextCompat.getDrawable(this, resource)
}

internal fun Int.isValueSet(): Boolean {
    return this != TooltipPopupConstants.NO_INT_VALUE
}

internal fun Long.isValueSet(): Boolean {
    return this != TooltipPopupConstants.NO_INT_VALUE.toLong()
}

internal fun Float.isValueSet(): Boolean {
    return this != TooltipPopupConstants.NO_INT_VALUE.toFloat()
}
