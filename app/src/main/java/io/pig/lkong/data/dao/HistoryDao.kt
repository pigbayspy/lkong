package io.pig.lkong.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.pig.lkong.data.entity.HistoryEntity

/**
 * @author yinhang
 * @since 2021/7/30
 */
@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity WHERE user_id = :userId LIMIT 20 OFFSET :start")
    suspend fun queryByUserIdAndTime(userId: Long, start: Int): List<HistoryEntity>

    @Insert
    suspend fun insert(history: HistoryEntity)

    @Query("DELETE FROM HistoryEntity WHERE user_id = :userId")
    suspend fun deleteByUser(userId: Long)
}