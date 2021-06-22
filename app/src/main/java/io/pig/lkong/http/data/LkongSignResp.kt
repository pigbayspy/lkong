package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/17
 */
data class LkongSignResp(
    val name: String,
    val uid: Long,
    val yousuu: String,
    val error: String?,
    val success: Boolean
)