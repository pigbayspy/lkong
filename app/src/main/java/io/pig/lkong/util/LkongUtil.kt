package io.pig.lkong.util

/**
 * 账户工具类
 *
 * @author yinhang
 * @since 2021/05/18
 */
object LkongUtil {

    fun generateAvatarUrl(userId: Long, avatar: String): String {
        return "https://image.lkong.com/avatar/$userId/$avatar"
    }

    fun generateForumAvatarUrl(fid: Long, avatar: String): String {
        return "https://image.lkong.com/forumavatar/${fid}/${avatar}"
    }
}