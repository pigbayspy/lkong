package io.pig.lkong.http.data.resp.data.common

class PostRate(
    val dateline: Long,
    val id: String,
    val num: Int,
    val reason: String,
    val user: AuthorData
)