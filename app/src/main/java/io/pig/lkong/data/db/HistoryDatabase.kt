package io.pig.lkong.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.pig.lkong.data.dao.HistoryDao
import io.pig.lkong.data.entity.HistoryEntity

/**
 * @author yinhang
 * @since 2021/7/30
 */
@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
}