package com.boyzdroizy.library.tooltipopwordtv.tooltipopupWindows

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import com.boyzdroizy.library.tooltipopwordtv.*
import com.boyzdroizy.library.tooltipopwordtv.annotations.Sp
import com.boyzdroizy.library.tooltipopwordtv.listeners.ToolTipListeners
import com.boyzdroizy.library.tooltipopwordtv.utils.ScreenSizeUtils.getLocationOnScreen
import com.boyzdroizy.library.tooltipopwordtv.utils.ScreenSizeUtils.getWidthWindow
import com.boyzdroizy.library.tooltipopwordtv.utils.TooltipPopupConstants.DEFAULT_DESCRIPTION
import com.boyzdroizy.library.tooltipopwordtv.utils.TooltipPopupConstants.NO_INT_VALUE
import com.boyzdroizy.library.tooltipopwordtv.utils.applyArrowCustomizer
import com.boyzdroizy.library.tooltipopwordtv.utils.applyDescriptionCustomizer
import com.boyzdroizy.library.tooltipopwordtv.utils.applyTextCustomizer
import com.boyzdroizy.library.tooltipopwordtv.wordTextView.SelectableWordTextView
import kotlinx.android.synthetic.main.default_tooltip_layout.view.*
import kotlinx.android.synthetic.main.dialog_tooltip.view.*

class ToolPopupWindows(
    private val context: Context,
    private val builder: ToolTipBuilder
) : ViewTreeObserver.OnGlobalLayoutListener {
    private val tipWindow: PopupWindow = PopupWindow(context)
    private lateinit var anchorRect: Rect
    private lateinit var contentView: View
    private lateinit var parent: SelectableWordTextView
    private var heightOfLines = 0

    init {
        initToolTip()
        initListeners()
        initializeArrowAnchor()
        initAutoDismissTooltip()
        if (builder.customLayout.isValueSet()) initCustomLayout()
        else {
            populateToolTipPopup()
            initTextCustomizer()
            initToolTipCustomizations()
        }
    }

    override fun onGlobalLayout() {
        val containerHeight = contentView.tooltipContainer.height
        val screenHeight = context.resources.configuration.screenHeightDp.px - 50

        if (anchorRect.top + containerHeight > screenHeight) {
            contentView.arrowAnchor.hide()
            contentView.arrowAnchorBottom.show()
            contentView.arrowAnchorBottom.layoutParams = contentView.arrowAnchor.layoutParams

            tipWindow.update(
                0,
                anchorRect.top - containerHeight + heightOfLines - space,
                contentView.tooltipContainer.width,
                containerHeight
            )
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
            isTouchable = true
            isFocusable = builder.isOutsideTouchable
            setBackgroundDrawable(BitmapDrawable())
            animationStyle = R.style.DialogScale
            windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        }
        tipWindow.contentView = contentView
        contentView.tooltipContainer.viewTreeObserver.addOnGlobalLayoutListener(this@ToolPopupWindows)
    }

    private fun initToolTipCustomizations() {
        with(contentView.defaultLayout.container) {
            if (builder.backgroundColor.isValueSet()) {
                val contentDrawable = DrawableCompat.wrap(this.background)
                DrawableCompat.setTint(contentDrawable, builder.backgroundColor)
                this.background = contentDrawable
            }

            if (builder.backgroundDrawable != null) {
                this.background = builder.backgroundDrawable
            }
        }
    }

    private fun populateToolTipPopup() {
        if (builder.textTitle.isNotEmpty()) contentView.title.text = builder.textTitle
        contentView.description.text = builder.toolTipDescription
    }

    private fun initTextCustomizer() {
        contentView.title.applyTextCustomizer(builder)
        contentView.description.applyDescriptionCustomizer(builder)
    }

    private fun initializeArrowAnchor() {
        with(contentView.arrowAnchor) {
            builder.arrowCustomised?.let {
                applyArrowCustomizer(it)
            }
        }

    }

    private fun initCustomLayout() {
        contentView.defaultLayout.hide()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewInflated = inflater.inflate(builder.customLayout, null)
        viewInflated.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
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

    private fun dismissSelected() = parent.dismissSelected()

    fun showToolTipAtLocation(
        anchorView: TextView,
        wordSelected: String,
        lineNumber: Int,
        width: Int
    ) {
        this.parent = anchorView as SelectableWordTextView
        val location = getLocationOnScreen(anchorView, context)
        anchorRect = Rect(
            location.x, location.y,
            location.x + anchorView.width,
            location.y + anchorView.height
        )
        heightOfLines = (anchorView.lineHeight - space) * lineNumber
        val positionY = anchorRect.top + heightOfLines

        val arrowParams = contentView.arrowAnchor.layoutParams as LinearLayout.LayoutParams
        val differenceOfWidth = (getWidthWindow(context).x - tipWindow.width) / 2

        arrowParams.leftMargin = anchorRect.left + width - differenceOfWidth

        tipWindow.showAtLocation(anchorView, Gravity.TOP or Gravity.CENTER, 0, positionY)
        contentView.arrowAnchor.layoutParams = arrowParams
        if (builder.textTitle.isEmpty()) contentView.title.text = wordSelected

    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun dismissTooltip() {
        if (tipWindow.isShowing) {
            dismissSelected()
            tipWindow.dismiss()
            contentView.tooltipContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    fun getCustomInflatedView(): View? {
        return if (builder.customLayout != NO_INT_VALUE) contentView.tooltipContent else null
    }

    @Suppress("MemberVisibilityCanBePrivate")
    class ToolTipBuilder(private val context: Context) {
        var width: Double = 0.8
        var textTitleColor: Int = NO_INT_VALUE
        var textTitleSize: Float = NO_INT_VALUE.toFloat()
        var textTitleTypeface = Typeface.NORMAL
        var textDescriptionColor: Int = NO_INT_VALUE
        var textDescriptionSize: Float = NO_INT_VALUE.toFloat()
        var textDescriptionTypeface = Typeface.NORMAL
        var backgroundColor: Int = NO_INT_VALUE
        var backgroundDrawable: Drawable? = null
        var customLayout: Int = NO_INT_VALUE
        var isOutsideTouchable: Boolean = true
        var textTitle: String = ""
        var toolTipDescription: String = DEFAULT_DESCRIPTION
        var autoDismissDuration: Long = NO_INT_VALUE.toLong()
        var toolTipListeners: ToolTipListeners? = null
        var arrowCustomised: ArrowCustomised? = null

        fun setWidthPercentsFromScreen(value: Double): ToolTipBuilder = apply { this.width = value }

        fun setTitleTextColor(@ColorInt value: Int): ToolTipBuilder =
            apply { this.textTitleColor = value }

        fun setTitleTextColorResource(@ColorRes value: Int): ToolTipBuilder = apply {
            this.textTitleColor = context.contextColor(value)
        }

        fun setTitleTextTypeface(value: Int): ToolTipBuilder =
            apply { this.textTitleTypeface = value }

        fun setTitleTextSize(@Sp value: Float): ToolTipBuilder =
            apply { this.textTitleSize = value }

        fun setBackgroundColor(value: Int): ToolTipBuilder = apply { this.backgroundColor = value }

        fun setBackgroundDrawable(@DrawableRes value: Int): ToolTipBuilder = apply {
            this.backgroundDrawable = context.contextDrawable(value)
        }

        fun setDescriptionTextColor(@ColorInt value: Int): ToolTipBuilder =
            apply { this.textDescriptionColor = value }

        fun setDescriptionTextColorResource(@ColorRes value: Int): ToolTipBuilder = apply {
            this.textTitleColor = context.contextColor(value)
        }

        fun setDescriptionTextTypeface(value: Int): ToolTipBuilder =
            apply { this.textDescriptionTypeface = value }

        fun setDescriptionTextSize(@Sp value: Float): ToolTipBuilder =
            apply { this.textDescriptionSize = value }

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

        fun setArrowCustomizer(value: ArrowCustomised): ToolTipBuilder =
            apply { this.arrowCustomised = value }

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