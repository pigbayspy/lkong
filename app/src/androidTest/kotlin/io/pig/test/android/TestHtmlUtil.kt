package io.pig.test.android

import io.pig.lkong.util.HtmlUtil
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author yinhang
 * @since 2021/6/27
 */
class TestHtmlUtil {

    @Test
    fun testParseHtml() {
        val expectText = "对《一个魔法中世纪社会：西欧》一书的原创翻译（连载）"
        val html =
            "<span class='datatitle'  style=\\\"font-weight: bold;color: #3C9D40\\\">$expectText</span>"
        val result = HtmlUtil.htmlToPlain(html)
        assertEquals(expectText, result, "parse error")
    }
}