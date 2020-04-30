package com.example.tooltipopbyword

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ecandrea.library.tooltipopwordtv.listeners.SelectableWordListeners
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ToolPopupWindows
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SelectableWordListeners {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        word.apply {
            text = "Select a word  from this text"
            setToolTipListener(this@MainActivity)
        }
    }

    override fun onWordSelected(
        anchorView: TextView,
        wordSelected: String,
        lineNumber: Int,
        width: Int
    ) {
        val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this)
            .build()
        word.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
    }
}
