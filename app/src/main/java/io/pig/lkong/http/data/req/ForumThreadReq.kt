package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/8/15
 */
class ForumThreadReq(fid: Int, page: Int = 1) {
    val operationName = "threads"
    val variables = mapOf<String, Any>("fid" to fid, "page" to page)
    val query = """
    query ViewUserCollections(${'$'}groupid: Int!) {
        collections: userFavoriteGroupList(uid: ${'$'}groupid) {
            ...CommentsComponent
        }
    }
    fragment CommentsComponent on FavoriteGroup {
        id
        title
        count
        uid
        isPublic
        description
        dateline
    }
    """
}