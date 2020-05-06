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
import kotlinx.android.synthetic.main.custom_layout.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        word.apply {
            text = "Select a word from this example."
            setToolTipListener(object : SelectableWordListeners {
                override fun onWordSelected(anchorView: TextView, wordSelected: String, lineNumber: Int, width: Int) {
                    val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this@MainActivity)
                            .setToolTipListener { Toast.makeText(applicationContext, "dismissed", Toast.LENGTH_SHORT).show() }
                            .setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                            .setTitleTextSize(20f)
                            .setAutoDismissDuration(1500)
                            .setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                            .setIsOutsideTouchable(false)
                            .setArrowCustomizer(ArrowCustomizer.Builder(this@MainActivity)
                                    .setArrowColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                                    .setArrowSize(20)
                                    .build())
                            .build()

                    word.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
                }
            })
        }
        word.setBackgroundWordColor(ContextCompat.getColor(this, R.color.colorAccent))


        wordTwo.apply {
            text = "Select a another word from this example."
            setToolTipListener(object : SelectableWordListeners {
                override fun onWordSelected(anchorView: TextView, wordSelected: String, lineNumber: Int, width: Int) {
                    val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this@MainActivity)
                            .setToolTipListener { Toast.makeText(applicationContext, "dismissed", Toast.LENGTH_SHORT).show() }
                            .setCustomLayout(R.layout.custom_layout)
                            .setAutoDismissDuration(1500)
                            .setIsOutsideTouchable(false)
                            .setArrowCustomizer(ArrowCustomizer.Builder(this@MainActivity)
                                    .setArrowColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                                    .setArrowSize(20)
                                    .build())
                            .build()

                    val inflatedView = toolPopupWindows.getCustomInflatedView()
                    inflatedView?.let {
                        it.newText.text = wordSelected
                        it.newText.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))

                        it.newText.setOnClickListener {
                            toolPopupWindows.dismissTooltip()
                        }
                    }

                    wordTwo.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
                }

            })

        }
        wordTwo.setBackgroundWordColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
    }

}
