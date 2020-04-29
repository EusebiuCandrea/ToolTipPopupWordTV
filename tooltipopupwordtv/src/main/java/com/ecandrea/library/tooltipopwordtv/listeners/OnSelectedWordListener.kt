package com.ecandrea.library.tooltipopwordtv.listeners

import android.widget.TextView

interface OnSelectedWordListener {
    fun onWordSelected(anchorView: TextView, selectedWord: String, startIndex: Int, lineNo: Int)
}