package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class TimelineReq(nextTime: Long) {
    val query = """
        query {
            feeds(onlyThread: false, nextTime: $nextTime) {
                data {
                    ...MixPostComponent
                }
                nextTime
            }
        }
        fragment MixPostComponent on MixPost {
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