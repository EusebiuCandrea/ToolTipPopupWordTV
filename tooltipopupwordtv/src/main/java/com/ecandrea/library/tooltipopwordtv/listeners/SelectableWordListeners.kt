package com.ecandrea.library.tooltipopwordtv.listeners

import android.widget.TextView

interface SelectableWordListeners {
    fun onWordSelected(anchorView: TextView, wordSelected: String, lineNumber: Int, width: Int)
}