package com.ecandrea.library.tooltipopwordtv

import android.content.Context
import android.widget.Toast


class ToolTipopup {

    
    fun toaster(c: Context?, message: String?) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }
}