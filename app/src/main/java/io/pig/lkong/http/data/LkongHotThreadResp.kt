package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/24
 */
data class LkongHotThreadResp(
    val thread: List<LkongHotThread>,
    val title: String,
    val id: String,
    val isok: Boolean
)