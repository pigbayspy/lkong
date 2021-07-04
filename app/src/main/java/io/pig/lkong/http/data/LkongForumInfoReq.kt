package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/7/4
 */
class LkongForumInfoReq(forumId: Long) {
    val action = "forumconfig_$forumId"
    val mod = "ajax"
}