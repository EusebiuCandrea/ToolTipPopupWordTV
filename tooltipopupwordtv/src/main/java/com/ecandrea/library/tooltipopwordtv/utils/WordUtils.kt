package com.ecandrea.library.tooltipopwordtv.utils

import com.ecandrea.library.tooltipopwordtv.model.WordInfo
import java.util.*

internal object WordUtils {

    private var sPunctuations: List<Char>? = null

    init {
        val arr = listOf(
            ',',
            '.',
            ';',
            '!',
            '"',
            '，',
            '。',
            '！',
            '；',
            '、',
            '：',
            '“',
            '”',
            '?',
            '？'
        )
        sPunctuations = arr
    }

    fun isChinese(ch: Char): Boolean {
        return !sPunctuations!!.contains(ch)
    }

    fun getEnglishWordIndices(content: String): List<WordInfo> {
        val separatorIndices =
            getSeparatorIndices(
                content,
                ' '
            )
        for (punctuation in sPunctuations!!) {
            separatorIndices.addAll(
                getSeparatorIndices(
                    content,
                    punctuation
                )
            )
        }
        separatorIndices.sort()
        val wordInfoList: MutableList<WordInfo> = ArrayList()
        var start = 0
        var end: Int
        for (i in separatorIndices.indices) {
            end = separatorIndices[i]
            if (start == end) {
                start++
            } else {
                val wordInfo =
                    WordInfo()
                wordInfo.start = start
                wordInfo.end = end
                wordInfoList.add(wordInfo)
                start = end + 1
            }
        }
        return wordInfoList
    }

    /**
     * Get every word's index array of text
     *
     * @param word the content
     * @param ch   separate char
     * @return index array
     */
    private fun getSeparatorIndices(
        word: String,
        ch: Char
    ): MutableList<Int> {
        var pos = word.indexOf(ch)
        val indices: MutableList<Int> = ArrayList()
        while (pos != -1) {
            indices.add(pos)
            pos = word.indexOf(ch, pos + 1)
        }
        return indices
    }

}