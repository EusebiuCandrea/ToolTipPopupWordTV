package com.ecandrea.library.tooltipopwordtv.tooltipopupWindows

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.ecandrea.library.tooltipopwordtv.R
import com.ecandrea.library.tooltipopwordtv.hide
import com.ecandrea.library.tooltipopwordtv.listeners.OnToolTipDismissListener
import com.ecandrea.library.tooltipopwordtv.utils.Constants
import com.ecandrea.library.tooltipopwordtv.utils.ScreenSizeUtils.getLocationOnScreen
import kotlinx.android.synthetic.main.default_tooltip_layout.view.*
import kotlinx.android.synthetic.main.dialog_tooltip.view.*

class ToolPopupWindows(private val context: Context) {
    private val tipWindow: PopupWindow = PopupWindow(context)
    private lateinit var contentView: View

    init {
        initToolTip()
    }

    private fun initToolTip() {
        val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(R.layout.dialog_tooltip, null)

        with(tipWindow) {
            width = (getWidthWindow().x * 0.9).toInt()
            height = LinearLayout.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            isTouchable = true
            isFocusable = true
            setBackgroundDrawable(BitmapDrawable())
            animationStyle = R.style.DialogScale
            windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        }

        tipWindow.contentView = contentView
    }

    private fun getWidthWindow(): Point {
        val size = Point()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        display.getSize(size)
        return size
    }

    fun dismissTooltip() {
        if (tipWindow.isShowing) {
            tipWindow.dismiss()
        }
    }

    fun onClosePressed(onClick: View.OnClickListener) {
        contentView.close.setOnClickListener(onClick)
    }

    fun onDismiss(onClick: PopupWindow.OnDismissListener) {
        tipWindow.setOnDismissListener(onClick)
    }

    fun setDescription(description: String) {
        contentView.description.text = description
    }

    fun customArrowAnchor() {
        with(contentView.arrowAnchor) {

        }
    }

    fun addCustomLayout(layout: Int): View? {
        contentView.defaultLayout.hide()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewInflated = inflater.inflate(layout, null)
        contentView.tooltipContent.addView(viewInflated)

        return viewInflated
    }

    fun showToolTipAtLocation(
            anchorView: TextView,
            wordSelected: String,
            lineNumber: Int,
            width: Int
    ) {

        val location = getLocationOnScreen(anchorView, context)
        val anchorRect = Rect(
                location.x, location.y,
                location.x + anchorView.width,
                location.y + anchorView.height
        )
        val heightOfLine = anchorView.lineHeight - space
        val positionY = anchorRect.top + (lineNumber * heightOfLine)

        val arrowParams = contentView.arrowAnchor.layoutParams as LinearLayout.LayoutParams
        val differenceOfWidth = (getWidthWindow().x - tipWindow.width) / 2

        arrowParams.leftMargin = anchorRect.left + width - differenceOfWidth

        tipWindow.showAtLocation(anchorView, Gravity.TOP or Gravity.CENTER, 0, positionY)
        contentView.arrowAnchor.layoutParams = arrowParams

        contentView.title.text = wordSelected

    }

    class ToolTipBuilder() {
        var textColor: Int = Constants.NO_INT_VALUE
        var backgroundColor: Int = Constants.NO_INT_VALUE
        var customLayout: Int = Constants.NO_INT_VALUE
        var isOutsideTouchouble: Boolean = true
        var autoDismissDuration: Long = Constants.NO_INT_VALUE.toLong()
        var onToolTipDismiss: OnToolTipDismissListener? = null
    }

    companion object {
        const val space = 5
    }
}