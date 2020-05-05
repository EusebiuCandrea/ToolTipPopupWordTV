package com.ecandrea.library.tooltipopwordtv.utils

import android.content.res.ColorStateList
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import com.ecandrea.library.tooltipopwordtv.isValueSet
import com.ecandrea.library.tooltipopwordtv.isVisible
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ArrowCustomizer
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ToolPopupWindows

internal fun AppCompatImageView.applyArrowCustomizer(arrowCustomizer: ArrowCustomizer) {
    arrowCustomizer.arrowDrawable?.let {
        setImageDrawable(it)
    }

    if (arrowCustomizer.arrowColor.isValueSet()) ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(arrowCustomizer.arrowColor))
    if (arrowCustomizer.arrowSize.isValueSet()) layoutParams = LinearLayout.LayoutParams(arrowCustomizer.arrowSize, arrowCustomizer.arrowSize)
    isVisible(arrowCustomizer.arrowVisibility)
}

internal fun TextView.applyTextCustomizer(textCustomizer: ToolPopupWindows.ToolTipBuilder) {
    if (textCustomizer.textTitleSize.isValueSet()) textSize = textCustomizer.textTitleSize
    if (textCustomizer.textTitleColor.isValueSet()) setTextColor(textCustomizer.textTitleColor)
    setTypeface(typeface, textCustomizer.textTitleTypeface)
}

internal fun TextView.applyDescriptionCustomizer(textCustomizer: ToolPopupWindows.ToolTipBuilder) {
    if (textCustomizer.textDescriptionSize.isValueSet()) textSize = textCustomizer.textDescriptionSize
    if (textCustomizer.textDescriptionColor.isValueSet()) setTextColor(textCustomizer.textDescriptionColor)
    setTypeface(typeface, textCustomizer.textDescriptionTypeface)
}