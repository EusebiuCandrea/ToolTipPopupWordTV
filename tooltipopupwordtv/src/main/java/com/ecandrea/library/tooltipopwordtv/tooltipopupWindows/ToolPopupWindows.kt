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
import com.ecandrea.library.tooltipopwordtv.listeners.ToolTipListeners
import com.ecandrea.library.tooltipopwordtv.utils.ScreenSizeUtils.getLocationOnScreen
import com.ecandrea.library.tooltipopwordtv.utils.TooltipPopupConstants
import com.ecandrea.library.tooltipopwordtv.wordTextView.SelectableWordTextView
import kotlinx.android.synthetic.main.default_tooltip_layout.view.*
import kotlinx.android.synthetic.main.dialog_tooltip.view.*

class ToolPopupWindows(
        private val context: Context,
        private val builder: ToolTipBuilder
) {
    private val tipWindow: PopupWindow = PopupWindow(context)
    private lateinit var contentView: View
    private lateinit var parent: SelectableWordTextView

    init {
        initToolTip()
        initListeners()
        if (builder.customLayout != TooltipPopupConstants.NO_INT_VALUE) initCustomLayout()
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

    private fun initCustomLayout() {
        contentView.defaultLayout.hide()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewInflated = inflater.inflate(builder.customLayout, null)
        viewInflated.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        contentView.tooltipContent.addView(viewInflated)
    }

    private fun getWidthWindow(): Point {
        val size = Point()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        display.getSize(size)
        return size
    }

    private fun initListeners() {
        contentView.close.setOnClickListener {
            builder.toolTipListeners?.onCloseToolTip()
            dismissTooltip()
        }

        tipWindow.setOnDismissListener {
            builder.toolTipListeners?.onToolTipDismiss()
            dismissSelected()
        }
    }

    private fun dismissTooltip() {
        if (tipWindow.isShowing) {
            dismissSelected()
            tipWindow.dismiss()
        }
    }

    private fun dismissSelected() = parent.dismissSelected()

    fun showToolTipAtLocation(
            anchorView: TextView,
            wordSelected: String,
            lineNumber: Int,
            width: Int
    ) {
        this.parent = anchorView as SelectableWordTextView
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

    fun getCustomInflatedView(): View? {
        return if (builder.customLayout != TooltipPopupConstants.NO_INT_VALUE) contentView.tooltipContent else null
    }

    @Suppress("MemberVisibilityCanBePrivate")
    class ToolTipBuilder(private val context: Context) {
        var textColor: Int = TooltipPopupConstants.NO_INT_VALUE
        var backgroundColor: Int = TooltipPopupConstants.NO_INT_VALUE
        var customLayout: Int = TooltipPopupConstants.NO_INT_VALUE
        var isOutsideTouchable: Boolean = true
        var toolTipDescription: String = TooltipPopupConstants.DEFAULT_DESCRIPTION
        var autoDismissDuration: Long = TooltipPopupConstants.NO_INT_VALUE.toLong()
        var toolTipListeners: ToolTipListeners? = null

        fun setTextColor(value: Int): ToolTipBuilder = apply { this.textColor = value }

        fun setBackgroundColor(value: Int): ToolTipBuilder = apply { this.backgroundColor = value }

        fun setCustomLayout(value: Int): ToolTipBuilder = apply { this.customLayout = value }

        fun setAutoDismissDuration(value: Long): ToolTipBuilder =
                apply { this.autoDismissDuration = value }

        fun setIsOutsideTouchouble(value: Boolean): ToolTipBuilder =
                apply { this.isOutsideTouchable = value }

        fun setToolTipListener(listener: ToolTipListeners): ToolTipBuilder =
                apply {
                    this.toolTipListeners = listener
                }

        fun setToolTipListener(unit: () -> Unit): ToolTipBuilder = apply {
            this.toolTipListeners = object : ToolTipListeners {
                override fun onCloseToolTip() {
                    unit()
                }

                override fun onToolTipDismiss() {
                    unit()
                }
            }
        }

        fun build(): ToolPopupWindows =
                ToolPopupWindows(context = context, builder = this@ToolTipBuilder)
    }

    companion object {
        private const val space = 5
    }
}