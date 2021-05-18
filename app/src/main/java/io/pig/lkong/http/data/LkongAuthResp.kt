package io.pig.lkong.http.data

import io.pig.lkong.account.util.AccountUtil

/**
 * @author yinhang
 * @since 2021/5/18
 */
class LkongAuthResp(
    val name: String,
    val uid: Long,
    val yoosuu: String,
    val success: Boolean,
    val authCookie: String
) {
    val avatar = AccountUtil.generateAvatarUrl(uid)
}