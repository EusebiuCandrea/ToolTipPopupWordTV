package com.example.tooltipopbyword

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ecandrea.library.tooltipopwordtv.listeners.SelectableWordListeners
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ArrowCustomizer
import com.ecandrea.library.tooltipopwordtv.tooltipopupWindows.ToolPopupWindows
import com.ecandrea.library.tooltipopwordtv.wordTextView.SelectableWordTextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_layout.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDefaultTooltip()
        initCustomTooltip()
    }

    private fun initDefaultTooltip() {
        word.apply {
            text = text1
            setToolTipListener(object : SelectableWordListeners {
                override fun onWordSelected(anchorView: TextView, wordSelected: String, lineNumber: Int, width: Int) {
                    val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this@MainActivity)
                            .setToolTipListener { Toast.makeText(applicationContext, "dismissed", Toast.LENGTH_SHORT).show() }
                            .setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                            .setTitleTextSize(20f)
                            .setIsOutsideTouchable(false)
                            .setArrowCustomizer(ArrowCustomizer.Builder(this@MainActivity)
                                    .setArrowSize(14)
                                    .build())
                            .build()

                    word.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
                }
            })
        }
        word.setBackgroundWordColor(ContextCompat.getColor(this, R.color.colorAccent))
    }

    private fun initCustomTooltip() {
        wordTwo.apply {
            text = text2
            setToolTipListener(object : SelectableWordListeners {
                override fun onWordSelected(anchorView: TextView, wordSelected: String, lineNumber: Int, width: Int) {
                    val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this@MainActivity)
                            .setCustomLayout(R.layout.custom_layout)
                            .setIsOutsideTouchable(true)
                            .setWidthPercentsFromScreen(0.7)
                            .setAutoDismissDuration(2000)
                            .setArrowCustomizer(ArrowCustomizer.Builder(this@MainActivity)
                                    .setArrowColor(ContextCompat.getColor(this@MainActivity, R.color.arrow))
                                    .setArrowSize(25)
                                    .build())
                            .build()

                    initCustomView(toolPopupWindows, wordSelected, anchorView)

                    wordTwo.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
                }
            })
        }
        wordTwo.setBackgroundWordColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
    }

    private fun initCustomView(toolPopupWindows: ToolPopupWindows, wordSelected: String, anchorView: TextView) {
        val inflatedView = toolPopupWindows.getCustomInflatedView()
        inflatedView?.let {
            it.newText.text = wordSelected
            it.newDescription.text = "Press remove button to delete this word !"
            it.testButton.setOnClickListener {
                removedWord(anchorView, wordSelected, wordTwo)
                toolPopupWindows.dismissTooltip()
            }
        }
    }

    private fun removedWord(anchorView: TextView, wordSelected: String, wordTwo: SelectableWordTextView) {
        val text = anchorView.text.toString()
        val index = text.indexOf(wordSelected)
        wordTwo.text = text.removeRange(index, (index + wordSelected.length + 1))
    }

    companion object {
        const val text1 = "No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to " +
                "pursue pleasure rationally encounter consequences that are extremely painful."

        const val text2 = "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible " +
                "vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided " +
                "by arches into stiff sections."
    }
}
