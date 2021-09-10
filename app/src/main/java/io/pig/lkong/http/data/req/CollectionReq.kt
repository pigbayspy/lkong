package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/8/8
 */
class CollectionReq(uid: Long) {
    val query = """
    query {
        userFavoriteGroupList(uid: $uid) {
            id
            title
            count
            uid
            isPublic
            description
            dateline
        }
    }
    """
}