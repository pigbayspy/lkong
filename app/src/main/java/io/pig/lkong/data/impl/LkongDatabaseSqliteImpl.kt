package io.pig.lkong.data.impl

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.data.db.HistoryDatabase
import io.pig.lkong.data.entity.HistoryEntity
import io.pig.lkong.data.provider.cacahe.CacheObjectCursor
import io.pig.lkong.data.provider.cacahe.CacheObjectSelection
import io.pig.lkong.http.data.resp.data.NoticeRespData
import io.pig.lkong.model.HistoryModel
import io.pig.lkong.notice.NoticeCacheConst
import io.pig.lkong.util.JsonUtil
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

    private val contentResolver: ContentResolver = context.contentResolver

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

    override fun loadNotice(userId: Long): NoticeRespData? {
        val json = getCachedValue(NoticeCacheConst.generateNoticeCountKey(userId)) ?: ""
        return JsonUtil.fromJsonOfNull(json, NoticeRespData::class.java)
    }

    private fun getCachedValue(key: String): String? {
        val cacheSelection = CacheObjectSelection()
        cacheSelection.cacheKey(key)
        val cursor: CacheObjectCursor = cacheSelection.query(contentResolver)!!
        return if (cursor.count > 0) {
            cursor.moveToFirst()
            val json = cursor.cacheValue()
            cursor.close()
            json
        } else {
            null
        }
    }
}