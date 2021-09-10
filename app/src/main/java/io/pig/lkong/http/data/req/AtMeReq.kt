package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class AtMeReq(date:Long) {
    val query =
    """
    query {
        atme(date: $date) {
            data: posts {
                ...MixPostComponent
            }
            hasMore
            nextDate
        }
    }
    fragment MixPostComponent on MixPost {
        author {
            uid
            name
            avatar
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
                    avatar
                }
            }
        }
        ... on MinorPost {
            quote {
                pid
                author {
                    uid
                    name
                    avatar
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