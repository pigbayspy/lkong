package io.pig.lkong.http.data.resp.data

data class PostRespThreadData(
    val title: String,
    val tid: Long,
    val forum: ForumData,
    val replies: Int,
    val views: Int,
    val status: String,
    val lock: Boolean,
    val first: String,
    val dateline: Long,
    val lastpost: Long,
    val author: ThreadAuthorData
) {
    data class ThreadAuthorData(val uid: Long, val name: String, val avatar: String)

    data class ForumData(val fid: Long, val name: String)
}