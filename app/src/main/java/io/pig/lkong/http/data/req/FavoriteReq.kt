package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/8/15
 */
class FavoriteReq(groupId: Long) {
    val query = """
    query {
        userFavoriteList(gid: $groupId) {
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
    }
    """
}