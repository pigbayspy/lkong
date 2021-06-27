package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/27
 */
class LkongPostListReq(
    thread: Long,
    page: Int
) {
    val mod = "data"
    val sars = "thread/${thread}/${page}"
}