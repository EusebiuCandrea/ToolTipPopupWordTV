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

        tipWindow.width = (getWithWindow().x * 0.9).toInt()
        tipWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

        tipWindow.isOutsideTouchable = true
        tipWindow.isTouchable = true
        tipWindow.isFocusable = true
        tipWindow.setBackgroundDrawable(BitmapDrawable())
        tipWindow.animationStyle = R.style.DialogScale
        tipWindow.contentView = contentView
        tipWindow.windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
    }

    private fun getWithWindow(): Point {
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

    fun showToolTip(anchorView: TextView, wordSelected: String, lineNumber: Int, width: Int) {

        val location = getLocationOnScreen(anchorView, context)
        val anchorRect = Rect(location.x, location.y, location.x + anchorView.width, location.y + anchorView.height)
        val heightOfLine = anchorView.lineHeight - space
        val positionY = anchorRect.top + (lineNumber * heightOfLine)

        val arrowParams = contentView.arrowAnchor.layoutParams as LinearLayout.LayoutParams
        val differenceOfWidth = (getWithWindow().x - tipWindow.width) / 2

        arrowParams.leftMargin = anchorRect.left + width - differenceOfWidth

        tipWindow.showAtLocation(anchorView, Gravity.TOP or Gravity.CENTER, 0, positionY)
        contentView.arrowAnchor.layoutParams = arrowParams

        contentView.title.text = wordSelected

    }

    companion object {
        const val space = 5
    }

}