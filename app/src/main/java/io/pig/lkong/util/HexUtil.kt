package io.pig.lkong.util

import okhttp3.internal.and

/**
 * 16进制工具类
 *
 * @author yinhang
 * @since 2021/05/18
 */
object HexUtil {

    fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v: Int = element and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString()
    }
}