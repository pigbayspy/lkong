package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserPunchRespData(
    val isPunch: Boolean,
    val punchallday: Int,
    val punchday: Int,
    val punchhighestday: Int
)