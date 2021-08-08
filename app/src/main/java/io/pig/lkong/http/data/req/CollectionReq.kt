package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/8/8
 */
class CollectionReq(uid: Long) {
    val operationName = "ViewUserCollectionsPage"
    val variables = mapOf<String, Any>("uid" to uid)
    val query = """
    query ViewUserCollectionsPage(${'$'}uid: Int!) {
        collections: userFavoriteGroupList(uid: ${'$'}uid) {
            ...CollectionsComponent
        }
    }
    fragment CollectionsComponent on FavoriteGroup {
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