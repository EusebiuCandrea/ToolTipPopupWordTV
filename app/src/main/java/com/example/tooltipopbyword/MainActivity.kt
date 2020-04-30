package com.example.tooltipopbyword

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ArrowCustomizer
import com.ecandrea.library.tooltipopwordtv.listeners.SelectableWordListeners
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ToolPopupWindows
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_layout.view.*

class MainActivity : AppCompatActivity(), SelectableWordListeners {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        word.apply {
            text = "Select a word from this example."
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
                .setToolTipListener { Toast.makeText(applicationContext, "dismissed", Toast.LENGTH_SHORT).show() }
//                .setCustomLayout(R.layout.my_layout)
                .setAutoDismissDuration(1500)
                .setTextColor(resources.getColor(R.color.colorAccent))
                .setTextSize(20f)
//                .setTextTitle("Ceva")
                .setIsOutsideTouchable(false)
                .setArrowCustomizer(ArrowCustomizer.Builder(this)
                        .setArrowColor(resources.getColor(R.color.colorAccent))
                        .setArrowSize(20)
                        .build())
                .build()

        val viewC = toolPopupWindows.getCustomInflatedView()
//        viewC?.newText!!.text = wordSelected
        word.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
    }
}
