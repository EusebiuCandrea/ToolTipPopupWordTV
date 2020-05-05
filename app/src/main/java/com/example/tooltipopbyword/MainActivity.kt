package com.example.tooltipopbyword

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ecandrea.library.tooltipopwordtv.listeners.SelectableWordListeners
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ArrowCustomizer
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ToolPopupWindows
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SelectableWordListeners {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        word.apply {
            text = "Select a word from this example."
            setToolTipListener(this@MainActivity)
        }
        word.setBackgroundWordColor(ContextCompat.getColor(this, R.color.colorAccent))
    }

    override fun onWordSelected(
            anchorView: TextView,
            wordSelected: String,
            lineNumber: Int,
            width: Int
    ) {

        val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this)
                .setToolTipListener { Toast.makeText(applicationContext, "dismissed", Toast.LENGTH_SHORT).show() }
//                .setCustomLayout(R.layout.my_layout)
                .setAutoDismissDuration(1500)
                .setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setTitleTextSize(20f)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                .setTextTitle("Ceva")
                .setIsOutsideTouchable(false)
                .setArrowCustomizer(ArrowCustomizer.Builder(this)
                        .setArrowColor(ContextCompat.getColor(this, R.color.colorAccent))
                        .setArrowSize(20)
                        .build())
                .build()

        val viewC = toolPopupWindows.getCustomInflatedView()
//        viewC?.newText!!.text = wordSelected
        word.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
    }
}
