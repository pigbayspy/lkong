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

    fun generateAvatarUrl(userId: Long, avatar: String): String {
        return "https://image.lkong.com/avatar/$userId/$avatar"
    }

    fun generateForumAvatarUrl(fid: Long, avatar: String): String {
        return "https://image.lkong.com/forumavatar/${fid}/${avatar}"
    }

    fun parseTimelineContent(content: String): List<TimelineContentModel> {
        return try {
            val result = gson.fromJson<List<TimelineContentModel>>(content, timelineContentType)
            result
        } catch (e: Exception) {
            Log.e("LkongUtil", "parse string [$content] error")
            emptyList()
        }
    }
}