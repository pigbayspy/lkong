package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/8/15
 */
data class FavoriteRespData(
    val pid: String,
    val dateline:Long,
    val thread: FavoriteThread,
    val author: FavoriteAuthor
) {
    data class FavoriteThread(
        val tid: Long,
        val title: String,
        val replies: Int,
        val fid: Long,
        val forumName: String
    )

    data class FavoriteAuthor(
        val uid: Long,
        val name: String,
        val avatar: String
    )
}