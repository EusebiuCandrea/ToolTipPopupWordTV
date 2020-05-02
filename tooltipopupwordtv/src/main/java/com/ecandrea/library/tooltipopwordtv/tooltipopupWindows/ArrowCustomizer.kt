package com.ecandrea.library.tooltipopwordtv.tooltipopupWindows

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import com.ecandrea.library.tooltipopwordtv.contextColor
import com.ecandrea.library.tooltipopwordtv.contextDrawable
import com.ecandrea.library.tooltipopwordtv.px
import com.ecandrea.library.tooltipopwordtv.utils.TooltipPopupConstants


class ArrowCustomizer(builder: Builder) {

    val arrowDrawable = builder.arrowDrawable
    val arrowSize = builder.arrowSize
    val arrowColor = builder.arrowColor
    var arrowVisibility: Boolean = builder.arrowVisibility

    @Suppress("MemberVisibilityCanBePrivate")
    class Builder(val context: Context) {
        var arrowDrawable: Drawable? = null
        var arrowSize: Int = TooltipPopupConstants.NO_INT_VALUE
        var arrowColor: Int = TooltipPopupConstants.NO_INT_VALUE
        var arrowVisibility: Boolean = true

        fun setArrowDrawable(value: Drawable?): Builder = apply { this.arrowDrawable = value }

        fun setArrowDrawableResource(@DrawableRes value: Int): Builder = apply {
            this.arrowDrawable = context.contextDrawable(value)
        }

        fun setArrowSize(@Px value: Int): Builder = apply { this.arrowSize = value.px }

        fun setArrowColor(@ColorInt value: Int): Builder = apply { this.arrowColor = value }

        fun setArrowColorResource(@ColorInt value: Int): Builder = apply {
            this.arrowColor = context.contextColor(value)
        }

        fun setArrowVisibility(isVisible: Boolean): Builder = apply {
            this.arrowVisibility = isVisible
        }

        fun build() = ArrowCustomizer(this)
    }
}