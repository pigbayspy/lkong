package io.pig.lkong.data

import io.pig.lkong.model.HistoryModel

/**
 * @author yinhang
 * @since 2021/6/14
 */
interface LkongDatabase {

    fun getHistory(uid: Long, start: Int): List<HistoryModel>

    fun clearHistory(uid: Long)
}