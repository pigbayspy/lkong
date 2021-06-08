package io.pig.lkong.util

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

    fun parse(str: String): Date {
        return format.parse(str)!!
    }
}