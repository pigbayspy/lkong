package io.pig.test

import io.pig.lkong.util.DateUtil
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * @author yinhang
 * @since 2021/6/8
 */
class TestDateUtil {

    @Test
    fun testParse() {
        val str = "2011-06-22 13:51:20"
        val date = DateUtil.parse(str)
        assertNotNull(date, "结果不为 NULL")
        val calendar: Calendar = GregorianCalendar()
        calendar.time = date
        assertEquals(2011, calendar.get(Calendar.YEAR), "获取年份错误")
        assertEquals(5, calendar.get(Calendar.MONTH), "获取月份错误")
        assertEquals(22, calendar.get(Calendar.DAY_OF_MONTH), "获取天数错误")

    }
}