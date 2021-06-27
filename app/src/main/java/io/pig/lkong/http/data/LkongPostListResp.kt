package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/27
 */
data class LkongPostListResp(
    val model: String,
    val replies: Int,
    val page: Int,
    val curtime: Long,
    val nexttime: Long,
    val isend: Int,
    val loadtime: Long,
    val tmp: String,
    val data: List<LkongPostItem>
)