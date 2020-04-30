package com.ecandrea.library.tooltipopwordtv.wordTextView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.ecandrea.library.tooltipopwordtv.R
import com.ecandrea.library.tooltipopwordtv.listeners.SelectableWordListeners
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ToolPopupWindows
import com.ecandrea.library.tooltipopwordtv.utils.WordUtils

class SelectableWordTextView : AppCompatTextView {

    //Will be initialize later
    private lateinit var tooltip: ToolPopupWindows
    private lateinit var selectableWordListener: SelectableWordListeners

    private var charSequence: CharSequence = ""
    private var bufferType: BufferType? = null
    private var spannableString: SpannableString? = null
    private var underlineSpan: UnderlineSpan? = null
    private var languageType = 0

    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    @SuppressLint("Recycle")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(attrs, R.styleable.SelectableWordTextView).apply {
            getInt(R.styleable.SelectableWordTextView_language, 0)
        }.also {
            it.recycle()
        }
    }

    override fun setText(text: CharSequence, type: BufferType) {
        charSequence = text
        bufferType = type
        highlightColor = Color.TRANSPARENT
        movementMethod = LinkMovementMethod.getInstance()
        setText()
    }

    private fun setText() {
        spannableString = SpannableString(charSequence)
        if (languageType == 0) {
            dealEnglish()
        } else {
            dealChinese()
        }
        super.setText(spannableString, bufferType)
    }

    private fun dealChinese() {
        charSequence.withIndex().forEach { (index, char) ->
            if (WordUtils.isChinese(char)) {
                spannableString?.setSpan(
                    clickableSpan,
                    index,
                    index + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun dealEnglish() {
        val wordInfoList = WordUtils.getEnglishWordIndices(charSequence.toString())
        wordInfoList.forEach { wordInfo ->
            spannableString?.setSpan(
                clickableSpan,
                wordInfo.start,
                wordInfo.end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun setSelectedSpan(tv: TextView) {
        if (underlineSpan == null) {
            underlineSpan = UnderlineSpan()
        } else {
            spannableString?.removeSpan(underlineSpan)
        }
        spannableString?.setSpan(
            underlineSpan,
            tv.selectionStart,
            tv.selectionEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        super@SelectableWordTextView.setText(spannableString, bufferType)

    }

    private fun initListeners() {
        tooltip.onClosePressed(OnClickListener {
            dismissSelected()
            tooltip.dismissTooltip()
        })

        tooltip.onDismiss(PopupWindow.OnDismissListener {
            dismissSelected()
        })
    }

    private fun dismissSelected() {
        spannableString!!.removeSpan(underlineSpan)
        super@SelectableWordTextView.setText(spannableString, bufferType)
    }

    private val clickableSpan: ClickableSpan
        get() = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val tv = widget as TextView
                val startIndex = tv.selectionStart
                val endIndex = tv.selectionEnd

                if (TextUtils.isEmpty(text) || startIndex == -1 || endIndex == -1) {
                    return
                }
                setSelectedSpan(tv)
                if (tv.onPreDraw()) {
                    tv.text.subSequence(startIndex, endIndex).toString().also {

                        val lineNumber = tv.layout.getLineForOffset(startIndex)
                        val leftSize = getWordLeftSize(tv, it, lineNumber, startIndex)

//                        tooltip.showToolTipAtLocation(tv, it, lineNumber + 1, leftSize)
                        selectableWordListener.onWordSelected(tv, it, lineNumber + 1, leftSize)
                        initListeners()
                    }
                }

            }

            override fun updateDrawState(text8Paint: TextPaint) {}
        }

    private fun getWordLeftSize(
        textView: TextView,
        word: String,
        lineNumber: Int,
        startIndex: Int
    ): Int {
        val textFound = textView.layout.getLineStart(lineNumber)
        val substring = textView.text.toString().substring(textFound, startIndex)

        val leftWords = Rect()
        val selectedWord = Rect()
        textView.paint.getTextBounds(substring, 0, substring.length, leftWords)
        textView.paint.getTextBounds(word, 0, word.length, selectedWord)
        return leftWords.width() + selectedWord.width() / 2
    }

    //Used for customization
    fun setToolTipListener(listener: SelectableWordListeners) {
        this.selectableWordListener = listener
    }

    fun showToolTipWindow(
        anchorView: TextView,
        wordSelected: String,
        lineNumber: Int,
        width: Int,
        toolPopupWindows: ToolPopupWindows
    ) {
        tooltip = toolPopupWindows
        tooltip.showToolTipAtLocation(anchorView, wordSelected, lineNumber, width)
    }
}