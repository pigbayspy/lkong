package io.pig.lkong.http.data.resp

/**
 * @author yinhang
 * @since 2021/5/18
 */
class SignInResp(
    val name: String = "",
    val uid: Long = 0,
    val success: Boolean = true,
    val authCookie: String = "",
    val avatar: String = ""
)