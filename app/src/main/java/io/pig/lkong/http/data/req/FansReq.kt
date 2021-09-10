package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class FansReq(uid: Long, page: Int) {
    val query = """
    query {
        fansList(uid: $uid, page: $page) {
            uid
            name
            avatar
            isFollow
        }
    }
    """
}