package com.ecandrea.library.tooltipopwordtv

import android.view.View
import android.widget.TextView

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.enableClick() {
    this.isEnabled = true
}

fun View.disableClick() {
    this.isEnabled = false
}
