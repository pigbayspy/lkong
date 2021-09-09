package io.pig.lkong.http.data.resp.data

import io.pig.lkong.http.data.resp.data.common.AuthorData

class PostRespPostData(
    val lou: Int,
    val rate: List<PostRate>?,
    val quote: PostQuote?,
    val content: String,
    val dateline: Long,
    val status: String,
    val pid: String,
    val tid: Long,
    val editTime: Long?,
    val user: AuthorData
) {

    class PostRate(
        val dateline: Long,
        val id: String,
        val num: Int,
        val reason: String,
        val user: AuthorData
    )

    class PostQuote(
        val content: String,
        val pid: String,
        val author: AuthorData
    )
}