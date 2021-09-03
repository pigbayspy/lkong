package io.pig.lkong.data.impl

import android.content.Context
import androidx.room.Room
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.data.db.HistoryDatabase
import io.pig.lkong.data.entity.HistoryEntity
import io.pig.lkong.model.HistoryModel
import java.util.*

/**
 * @author yinhang
 * @since 2021/6/14
 */
class LkongDatabaseSqliteImpl(context: Context) : LkongDatabase {

    private val db = Room.databaseBuilder(
        context,
        HistoryDatabase::class.java, "lkong"
    ).build()

    private val dao = db.historyDao()

    override suspend fun getHistory(uid: Long, start: Int): List<HistoryModel> {
        val entities = dao.queryByUserIdAndTime(uid, start)
        return entities.map {
            HistoryModel(
                userId = it.userId,
                threadId = it.threadId,
                threadTitle = it.threadTitle,
                forumId = it.forumId,
                postId = it.postId,
                forumTitle = it.forumTitle,
                authorId = it.authorId,
                authorName = it.authorName,
                lastReadTime = Date(it.lastReadTime)
            )
        }
    }

    override suspend fun clearHistory(uid: Long) {
        dao.deleteByUser(uid)
    }

    override suspend fun saveBrowseHistory(
        userId: Long,
        threadId: Long,
        threadTitle: String,
        forumId: Long,
        forumTitle: String,
        postId: String,
        authorId: Long,
        authorName: String
    ) {
        val entity = HistoryEntity(
            userId = userId,
            threadId = threadId,
            threadTitle = threadTitle,
            forumId = forumId,
            forumTitle = forumTitle,
            postId = postId,
            authorId = authorId,
            authorName = authorName,
            lastReadTime = System.currentTimeMillis()
        )
        dao.insert(entity)
    }
}