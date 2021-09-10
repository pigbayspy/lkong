package io.pig.lkong.http.data.req

class ThreadPostReq(tid: Long, page: Int) {
    val query = """
    query {
        thread(tid: $tid) {
            title
            tid
            forum {
                fid
                name
            }
            digest
            replies
            views
            status
            lock
            first
            dateline
            lastpost
            author {
                name
                uid
                avatar
            }
        }
        posts(tid:$tid, page:$page) {
            lou
            pid
            tid
            rate {
                dateline
                id
                num
                reason
                user {
                    name
                    uid
                    avatar
                }
            }
            quote {
                content
                pid
                author {
                    name
                    uid
                    avatar
                }
            }
            content
            dateline
            status
            editTime
            user {
                uid
                name
                avatar
            }
        }
    }
    """
}