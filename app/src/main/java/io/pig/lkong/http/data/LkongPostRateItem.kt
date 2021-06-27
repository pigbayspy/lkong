package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/27
 */
data class LkongPostRateItem(
    val dateline: String,
    val extcredits: Int,
    val pid: Long,
    val reason: String,
    val score: Int,
    val uid: Long,
    val username: String
)