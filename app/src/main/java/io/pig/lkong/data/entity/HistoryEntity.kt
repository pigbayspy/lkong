package io.pig.lkong.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * @author yinhang
 * @since 2021/7/30
 */
@Entity
data class HistoryEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "thread_id")
    val threadId: Long,
    @ColumnInfo(name = "thread_title")
    val threadTitle: String,
    @ColumnInfo(name = "forum_id")
    val forumId: Long,
    @ColumnInfo(name = "post_id")
    val postId: Long,
    @ColumnInfo(name = "forum_title")
    val forumTitle: String,
    @ColumnInfo(name = "author_id")
    val authorId: Long,
    @ColumnInfo(name = "author_name")
    val authorName: String,
    @ColumnInfo(name = "last_read_time")
    val lastReadTime: Date
)