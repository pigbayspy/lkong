package io.pig.lkong.http.data.req

class UserThreadReq(userId: Long, page: Int) {

    val query =
    """
    query {
        userThreads(uid: $userId, page:$page) {
            fid
            tid
            dateline
            replies
            title
            status
            lock
        }
    }
    """
}