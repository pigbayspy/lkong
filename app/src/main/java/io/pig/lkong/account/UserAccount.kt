package io.pig.lkong.account

import android.accounts.Account
import io.pig.lkong.http.util.CookieUtil
import okhttp3.Cookie
import java.io.Serializable

/**
 * 用户账户数据
 *
 * @author yinhang
 * @since 2021/5/16
 */
class UserAccount(
    val account: Account,
    val userId: Long,
    val userName: String,
    val userEmail: String,
    val userAvatar: String,
    cookie: String
) : Serializable {
    val authCookie: Cookie = CookieUtil.decode(cookie)
}