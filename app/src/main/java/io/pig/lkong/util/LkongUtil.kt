package io.pig.lkong.util

/**
 * 账户工具类
 *
 * @author yinhang
 * @since 2021/05/18
 */
object LkongUtil {

    fun generateAvatarUrl(userId: Long): String {
        val uidString = String.format("%1$06d", userId)
        return String.format(
            "http://img.lkong.cn/avatar/000/%s/%s/%s_avatar_middle.jpg",
            uidString.substring(0, 2),
            uidString.substring(2, 4),
            uidString.substring(4, 6)
        )
    }

    fun fidToForumIconUrl(fid: Long): String {
        val fidString = String.format("%1$06d", fid)
        return String.format(
            "http://img.lkong.cn/forumavatar/000/%s/%s/%s_avatar_middle.jpg",
            fidString.substring(0, 2),
            fidString.substring(2, 4),
            fidString.substring(4, 6)
        )
    }
}