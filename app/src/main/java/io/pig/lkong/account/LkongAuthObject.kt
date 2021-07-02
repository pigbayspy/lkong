package io.pig.lkong.account

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 * @author yinhang
 * @since 2021/5/16
 */
class LkongAuthObject(
    val userId: Long,
    val userName: String,
    val authUrl: HttpUrl,
    val authCookie: Cookie
)