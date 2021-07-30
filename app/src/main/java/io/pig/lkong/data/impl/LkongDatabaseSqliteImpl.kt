package io.pig.lkong.data.impl

import android.content.Context
import androidx.room.Room
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.data.db.HistoryDatabase
import io.pig.lkong.model.HistoryModel

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

    override fun getHistory(uid: Long, start: Int): List<HistoryModel> {
        val entities = dao.queryByUserIdAndTime(uid, start)
        val result = entities.map {
            HistoryModel(
                userId = it.userId,
                threadId = it.threadId,
                threadTitle = it.threadTitle,
                forumId = it.forumId,
                postId = it.postId,
                forumTitle = it.forumTitle,
                authorId = it.authorId,
                authorName = it.authorName,
                lastReadTime = it.lastReadTime
            )
        }
        return result
    }

    override fun clearHistory(uid: Long) {
        dao.deleteByUser(uid)
    }
}