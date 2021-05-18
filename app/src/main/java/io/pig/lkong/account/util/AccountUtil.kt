package io.pig.lkong.account.util

/**
 * 账户工具类
 *
 * @author yinhang
 * @since 2021/05/18
 */
object AccountUtil {

    fun generateAvatarUrl(userId: Long): String {
        val uidString = String.format("%1$06d", userId)
        return String.format(
            "http://img.lkong.cn/avatar/000/%s/%s/%s_avatar_middle.jpg",
            uidString.substring(0, 2),
            uidString.substring(2, 4),
            uidString.substring(4, 6)
        )
    }
}