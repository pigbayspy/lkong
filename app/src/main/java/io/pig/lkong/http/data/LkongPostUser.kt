package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/27
 */
data class LkongPostUser(
    val adminid: String,
    val customstatus: String,
    val gender: Int,
    val regdate: Long,
    val uid: Long,
    val username: String,
    val verify: Boolean,
    val verifymessage: String,
    val color: String,
    val stars: String,
    val ranktitle: String
)