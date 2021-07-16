package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserCountRespData(
    val followings: Long,
    val followers: Long,
    val posts: Long,
    val threads: Long,
    val money: Long,
    val longjing: Long,
    val vertyInfo: String?,
    val level: String
)