package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class UserProfileReq(uid: Long) {
    val query = """
    query {  
        user(uid: $uid) {
            uid
            name
            avatar
            verify {
                type
                info
            }
            status
            dateline
        }
        userCount(uid: $uid) {
            followings
            followers
            posts
            threads
            money
            longjing
            vertyInfo
            level {
                name
                color
            }
        }
        userPunch(uid: $uid) {
            isPunch
            punchallday
            punchday
            punchhighestday
        }
    }
    """
}