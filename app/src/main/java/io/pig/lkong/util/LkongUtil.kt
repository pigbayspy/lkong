package io.pig.lkong.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.pig.lkong.model.TimelineContentModel

/**
 * 账户工具类
 *
 * @author yinhang
 * @since 2021/05/18
 */
object LkongUtil {

    private val gson = Gson()

    private val timelineContentType = object : TypeToken<List<TimelineContentModel>>() {}.type

    fun generateAvatarUrl(userId: Long): String {
        val uidString = String.format("%1$06d", userId)
        return String.format(
            "http://img.lkong.cn/avatar/000/%s/%s/%s_avatar_middle.jpg",
            uidString.substring(0, 2),
            uidString.substring(2, 4),
            uidString.substring(4, 6)
        )
    }

    fun generateAvatarUrl(userId: Long, avatar: String): String {
        return "https://image.lkong.com/avatar/$userId/$avatar"
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

    fun parseTimelineContent(content: String): List<TimelineContentModel> {
        try {
            val result = gson.fromJson<List<TimelineContentModel>>(content, timelineContentType)
            return result
        } catch (e: Exception) {
            Log.e("LkongUtil", "parse string [$content] error")
            return emptyList()
        }
    }
}