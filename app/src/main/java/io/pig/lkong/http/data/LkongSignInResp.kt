package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/5/18
 */
class LkongSignInResp(
    val name: String = "",
    val uid: Long = 0,
    val success: Boolean = true,
    val authCookie: String = "",
    val avatar: String = ""
)