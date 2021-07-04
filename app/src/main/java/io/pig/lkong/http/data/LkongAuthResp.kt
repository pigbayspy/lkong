package io.pig.lkong.http.data

import io.pig.lkong.util.LkongUtil

/**
 * @author yinhang
 * @since 2021/5/18
 */
class LkongAuthResp(
    val name: String,
    val uid: Long,
    val yousuu: String,
    val success: Boolean,
    val authCookie: String,
    val discussCookie: String
) {
    val avatar = LkongUtil.generateAvatarUrl(uid)
}