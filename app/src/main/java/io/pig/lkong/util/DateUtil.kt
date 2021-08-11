package io.pig.lkong.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期工具类
 *
 * @author yinhang
 * @since 2021/6/8
 */
object DateUtil {

    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    private val shortFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

    fun parse(str: String): Date {
        return format.parse(str)!!
    }

    fun formatDateByToday(date: Date, todayPrefix: String): String {
        if (DateUtils.isToday(date.time)) {
            return todayPrefix + shortFormat.format(date)
        }
        return fullFormat.format(date)
    }

    fun formatDateByTimestamp(dateline: Long): String {
        val date = Date(dateline)
        return format.format(date)
    }
}