package io.pig.lkong.http.data.req

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