package io.pig.lkong.util

import android.text.Html
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.pig.common.ui.slate.SlateNode

/**
 * @author yinhang
 * @since 2021/8/18
 */
object SlateUtil {

    private val gson = Gson()

    private val slateType = object : TypeToken<List<SlateNode>>() {}.type

    fun slateToText(slate: String): String {
        val slateList = gson.fromJson<List<SlateNode>>(slate, slateType)
        val slateHtml = buildString {
            slateList.forEach {
                append(it.toString())
            }
        }
        return Html.fromHtml(slateHtml, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }

    fun slateToCleanText(slate: String): String {
        return slateToText(slate).trim('\n')
    }
}