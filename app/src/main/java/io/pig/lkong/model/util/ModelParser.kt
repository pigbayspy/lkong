package io.pig.lkong.model.util

import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.model.ThreadModel

/**
 * @author yinhang
 * @since 2021/6/8
 */
object ModelParser {

    /**
     * 将获取帖子列表的返回值转化为 ListModel 列表
     */
    fun toForumThreadList(threadList: LkongForumThreadResp): List<ThreadModel> {
        if (threadList.data.isNullOrEmpty()) {
            return emptyList()
        }
        return threadList.data.map {
            ThreadModel(it)
        }
    }
}