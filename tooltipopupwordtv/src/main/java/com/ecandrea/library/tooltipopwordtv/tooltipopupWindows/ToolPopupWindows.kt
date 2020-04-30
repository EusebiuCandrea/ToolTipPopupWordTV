package com.ecandrea.library.tooltipopwordtv.tooltipopupWindows

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.ecandrea.library.tooltipopwordtv.R
import com.ecandrea.library.tooltipopwordtv.annotations.Sp
import com.ecandrea.library.tooltipopwordtv.contextColor
import com.ecandrea.library.tooltipopwordtv.hide
import com.ecandrea.library.tooltipopwordtv.isValueSet
import com.ecandrea.library.tooltipopwordtv.listeners.ToolTipListeners
import com.ecandrea.library.tooltipopwordtv.utils.ScreenSizeUtils.getLocationOnScreen
import com.ecandrea.library.tooltipopwordtv.utils.ScreenSizeUtils.getWidthWindow
import com.ecandrea.library.tooltipopwordtv.utils.TooltipPopupConstants.DEFAULT_DESCRIPTION
import com.ecandrea.library.tooltipopwordtv.utils.TooltipPopupConstants.NO_INT_VALUE
import com.ecandrea.library.tooltipopwordtv.utils.applyArrowCustomizer
import com.ecandrea.library.tooltipopwordtv.utils.applyTextCustomizer
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
        initializeArrowAnchor()
        initAutoDismissTooltip()
        if (builder.customLayout.isValueSet()) initCustomLayout()
        else {
            populateToolTipPopup()
            initTextCustomizer()
        }
    }

    private fun initToolTip() {
        val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        contentView = inflater.inflate(R.layout.dialog_tooltip, null)

        with(tipWindow) {
            width = (getWidthWindow(context).x * builder.width).toInt()
            height = LinearLayout.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = builder.isOutsideTouchable
            isTouchable = builder.isOutsideTouchable
            isFocusable = builder.isOutsideTouchable
            setBackgroundDrawable(BitmapDrawable())
            animationStyle = R.style.DialogScale
            windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        }

        tipWindow.contentView = contentView
    }

    private fun populateToolTipPopup() {
        if (builder.textTitle.isNotEmpty()) contentView.title.text = builder.textTitle
        contentView.description.text = builder.toolTipDescription
    }

    private fun initTextCustomizer() {
        contentView.title.applyTextCustomizer(builder)
        contentView.description.applyTextCustomizer(builder)

    private fun initializeArrowAnchor() {
        with(contentView.arrowAnchor) {
            builder.arrowCustomizer?.let {
                applyArrowCustomizer(it)
            }
        }

    private fun initCustomLayout() {
        contentView.defaultLayout.hide()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewInflated = inflater.inflate(builder.customLayout, null)
        viewInflated.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        contentView.tooltipContent.addView(viewInflated)
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

    private fun initAutoDismissTooltip() {
        if (builder.autoDismissDuration.isValueSet()) {
            Handler().postDelayed({
                dismissTooltip()
            }, builder.autoDismissDuration)
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
        val differenceOfWidth = (getWidthWindow(context).x - tipWindow.width) / 2

        arrowParams.leftMargin = anchorRect.left + width - differenceOfWidth

        tipWindow.showAtLocation(anchorView, Gravity.TOP or Gravity.CENTER, 0, positionY)
        contentView.arrowAnchor.layoutParams = arrowParams
        if (builder.textTitle.isEmpty()) contentView.title.text = wordSelected

    }

    fun getCustomInflatedView(): View? {
        return if (builder.customLayout != NO_INT_VALUE) contentView.tooltipContent else null
    }

    @Suppress("MemberVisibilityCanBePrivate")
    class ToolTipBuilder(private val context: Context) {
        var width: Double = 0.8
        var textColor: Int = NO_INT_VALUE

        @Sp
        var textSize: Float = NO_INT_VALUE.toFloat()
        var textTypeface = Typeface.NORMAL
        var backgroundColor: Int = NO_INT_VALUE
        var customLayout: Int = NO_INT_VALUE
        var isOutsideTouchable: Boolean = true
        var textTitle: String = ""
        var toolTipDescription: String = DEFAULT_DESCRIPTION
        var autoDismissDuration: Long = NO_INT_VALUE.toLong()
        var toolTipListeners: ToolTipListeners? = null
        var arrowCustomizer: ArrowCustomizer? = null

        fun setWidthPercentsFromScreen(value: Double): ToolTipBuilder = apply { this.width = value }

        fun setTextColor(@ColorInt value: Int): ToolTipBuilder = apply { this.textColor = value }

        fun setTextColorResource(@ColorRes value: Int): ToolTipBuilder = apply {
            this.textColor = context.contextColor(value)
        }

        fun setTextTypeface(value: Int): ToolTipBuilder = apply { this.textTypeface = value }

        fun setTextSize(@Sp value: Float): ToolTipBuilder = apply { this.textSize = value }

        fun setBackgroundColor(value: Int): ToolTipBuilder = apply { this.backgroundColor = value }

        fun setCustomLayout(value: Int): ToolTipBuilder = apply { this.customLayout = value }

        fun setAutoDismissDuration(value: Long): ToolTipBuilder =
                apply { this.autoDismissDuration = value }

        fun setIsOutsideTouchable(value: Boolean): ToolTipBuilder =
                apply { this.isOutsideTouchable = value }

        fun setToolTipListener(listener: ToolTipListeners): ToolTipBuilder =
                apply {
                    this.toolTipListeners = listener
                }

        fun setToolTipDescription(value: String): ToolTipBuilder = apply {
            this.toolTipDescription = value
        }

        fun setTextTitle(value: String): ToolTipBuilder = apply {
            this.textTitle = value
        }

        fun setArrowCustomizer(value: ArrowCustomizer): ToolTipBuilder = apply { this.arrowCustomizer = value }

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