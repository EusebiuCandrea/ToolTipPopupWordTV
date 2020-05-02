package com.ecandrea.library.tooltipopwordtv.utils

import com.ecandrea.library.tooltipopwordtv.model.WordInfo
import java.util.*

internal object WordUtils {

    private val punctuations: List<Char>? = listOf(',', '.', ';', '!', '"', '，', '。', '！', '；', '、', '：', '“', '”', '?', '？')

    private fun createWordInfoList(separatorIndices: MutableList<Int>): MutableList<WordInfo> {
        val wordInfoList: MutableList<WordInfo> = ArrayList()
        var start = 0
        var end: Int
        separatorIndices.forEach { separator ->
            end = separator
            if (start == end) {
                start++
            } else {
                WordInfo().apply {
                    this.start = start
                    this.end = end
                }.also {
                    wordInfoList.add(it)
                }
                start = end + 1
            }
        }

        return wordInfoList
    }

    private fun getSeparatorIndices(word: String, char: Char): MutableList<Int> {
        var pos = word.indexOf(char)
        val indices: MutableList<Int> = ArrayList()
        while (pos != -1) {
            indices.add(pos)
            pos = word.indexOf(char, pos + 1)
        }
        return indices
    }

    fun getWordIndices(content: String): List<WordInfo> {
        val separatorIndices =
                getSeparatorIndices(
                        content,
                        ' '
                )
        for (punctuation in punctuations!!) {
            separatorIndices.addAll(
                    getSeparatorIndices(
                            content,
                            punctuation
                    )
            )
        }
        separatorIndices.sort()

        return createWordInfoList(separatorIndices)
    }
}