package io.pig.lkong.http.data.resp.data

data class PostRespPostData(
    val lou: Int,
    val rate: List<PostRate>?,
    val quote: PostQuote?,
    val content: String,
    val dateline: Long,
    val status: String,
    val pid: String,
    val tid: Long,
    val editTime: Long?,
    val user: PostAuthor
) {

    data class PostAuthor(val uid: Long, val name: String, val avatar: String?)

    data class PostRate(
        val dateline: Long,
        val id: String,
        val num: Int,
        val reason: String,
        val user: PostRateAuthor
    ) {
        data class PostRateAuthor(val name: String, val uid: Long)
    }

    data class PostQuote(
        val content: String,
        val pid: String,
        val author: PostQuoteAuthor
    ) {
        data class PostQuoteAuthor(val name: String, val uid: Long)
    }
}