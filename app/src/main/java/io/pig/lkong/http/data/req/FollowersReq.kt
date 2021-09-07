package io.pig.lkong.http.data.req

class FollowersReq(uid: Long, page: Int) {
    val query = """
    query {
        followList(uid: $uid, page: $page) {
            uid
            name
            avatar
            isFollow
        }
    }
    """
}