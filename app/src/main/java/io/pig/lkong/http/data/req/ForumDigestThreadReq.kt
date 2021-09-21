package io.pig.lkong.http.data.req

class ForumDigestThreadReq(fid: Long, page: Int = 1) {
    val query = """
    query {
        threads(fid: $fid, page: $page, action: "digest") {
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