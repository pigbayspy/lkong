package io.pig.lkong.http.data.req

class ThreadPostReq(tid:Long, page:Int) {
    val operationName = "ViewThread"
    val variables = mapOf<String, Any>("tid" to tid, "page" to page)
    val query = """
    query ViewThread(${'$'}tid: Int!, ${'$'}page:Int!) {
        ...indexThreadComponent
    }
    fragment indexThreadComponent on Query {
        thread(tid: ${'$'}tid) {
            title
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
        posts(tid:${'$'}tid, page:${'$'}page) {
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
                }
            }
            quote {
                content
                pid
                author {
                    name
                    uid
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