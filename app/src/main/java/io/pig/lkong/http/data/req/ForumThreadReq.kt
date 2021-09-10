package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class ForumThreadReq(fid: Long, page: Int = 1) {
    val query = """
    query {
        threads(fid: $fid, page: $page) {
            tid
            fid
            title
            status
            lock
            replies
            views
            digest
            dateline
            lastpost
            highlight
            author {
                name
                uid
                avatar
            }
        }
    }
    """
}