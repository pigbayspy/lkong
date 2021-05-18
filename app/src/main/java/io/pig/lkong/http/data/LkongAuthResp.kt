package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/5/18
 */
class LkongAuthResp(
    var userEmail: String? = null,
    var userName: String? = null,
    var userId: Long = 0,
    var userAvatar: String? = null,
    var combinedCookie: String? = null
)