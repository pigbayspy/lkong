package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/8/15
 */
class FavoriteReq(groupId: Long) {
    val operationName = "ViewUserFavorites"
    val variables = mapOf("groupid" to groupId)
    val query = """
    query ViewUserFavorites(${'$'}groupid: Int!) {
        comments: userFavoriteList(gid: ${'$'}groupid) {
            ...CommentsComponent
        }
    }
    fragment CommentsComponent on MajorPost {
        pid
        dateline
        author {
            uid
            name
            avatar
        }
        thread {
            tid
            title
            replies
            fid
            forumName
        }
    }
    """
}