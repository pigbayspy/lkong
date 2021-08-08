package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/8/8
 */
data class CollectionData(
    val id: Long,
    val title: String,
    val count: Int,
    val uid: Long,
    val isPublic: Boolean,
    val description: String,
    val dateline: Long
)