package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/7/21
 */
class TimelineReq(nextTime: Long) {

    val operationName = "ViewNextFeeds"

    val variables = mapOf<String, Any>("nextTime" to nextTime, "onlyThread" to false)

    val query = """
        query ViewNextFeeds(${'$'}onlyThread: Boolean!, ${'$'}nextTime: Date) {
            feeds(onlyThread: ${'$'}onlyThread, nextTime: ${'$'}nextTime) {
                data {
                    ...MixPostComponent
                }
                nextTime
            }
        }
        fragment MixPostComponent on MixPost {
            authorid
            author {
                uid
                name
                avatar
                verify {
                    info
                    type
                }
            }
            dateline
            content
            pid
            ... on MajorPost {
                thread {
                    tid
                    title
                    replies
                    fid
                    forumName
                }
            }
            ... on MajorReply {
                thread {
                    tid
                    title
                    authorid
                    author {
                        uid
                        name
                    }
                }
            }
            ... on MinorPost {
                quote {
                    pid
                    author {
                        uid
                        name
                    }
                    content
                }
                thread {
                    tid
                }
            }
        }
    """
}