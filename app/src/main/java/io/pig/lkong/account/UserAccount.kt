package io.pig.lkong.account

import android.accounts.Account
import io.pig.lkong.http.cookie.SerializableCookie
import okhttp3.Cookie
import okhttp3.HttpUrl
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
    serializedAuthCookie: String
) : Serializable {

    val authUrl: HttpUrl
    val authCookie: Cookie

    init {
        val auth = SerializableCookie.decode(serializedAuthCookie)
        this.authUrl = auth.first
        this.authCookie = auth.second
    }
}