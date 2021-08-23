package io.pig.lkong.data

import io.pig.lkong.model.HistoryModel

/**
 * @author yinhang
 * @since 2021/6/14
 */
interface LkongDatabase {

    suspend fun getHistory(uid: Long, start: Int): List<HistoryModel>

    suspend fun clearHistory(uid: Long)

    suspend fun saveBrowseHistory(
        userId: Long,
        threadId: Long,
        threadTitle: String,
        forumId: Long,
        forumTitle: String,
        postId: Long,
        authorId: Long,
        authorName: String
    )
}