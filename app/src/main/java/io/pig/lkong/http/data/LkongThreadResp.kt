package io.pig.lkong.http.data

import java.util.*

/**
 * @author yinhang
 * @since 2021/5/24
 */
class LkongThreadResp(
    val sortKey: Long = 0,

    val sortKeyTime: Date,

    val dateline: Date,

    val subject: String,

    val userName: String,

    val digest: Boolean,

    val closed: Int,

    val uid: Long,

    val replyCount: Int,

    val id: String,

    val fid: Long,

    val userIcon: String
)