package io.pig.lkong.util

import android.text.Html
import androidx.core.text.HtmlCompat

/**
 * html 工具类
 *
 * @author yinhang
 * @since 2021/6/27
 */
object HtmlUtil {

    /**
     * html 转换为普通文本
     */
    fun htmlToPlain(html: String): String {
        return Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }
}