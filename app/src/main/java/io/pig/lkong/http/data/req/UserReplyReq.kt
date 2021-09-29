package io.pig.lkong.http.data.req

class UserReplyReq(uid: Long, page: Int, digest: Boolean) {
    val query = """
    query {
        userReplies(uid:$uid, page:$page, isDigest:$digest) {
            ...indexContent
        }
    }
    fragment indexContent on MixPost {
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