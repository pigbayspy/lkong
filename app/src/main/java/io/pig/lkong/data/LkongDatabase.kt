package io.pig.lkong.data

import io.pig.lkong.model.BrowseHistoryModel

/**
 * @author yinhang
 * @since 2021/6/14
 */
interface LkongDatabase {

    fun init()

    fun close()

    fun getBrowseHistory(uid: Long, start: Int): List<BrowseHistoryModel>

    fun getBrowseHistory(start: Int): List<BrowseHistoryModel>
    
    fun clearBrowserHistory(uid: Long)
}