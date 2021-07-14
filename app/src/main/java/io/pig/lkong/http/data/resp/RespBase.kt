package io.pig.lkong.http.data.resp

/**
 * @author yinhang
 * @since 2021/7/10
 */
data class RespBase<T>(
    val data: T?
    val errors: List<RespError>?
)