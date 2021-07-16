package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserProfileReq {

    val operationName = "ViewIndex"

    val query = """
        query ViewIndex {  
            ...indexConfigComponent
        }
        fragment indexConfigComponent on Query {
            user(uid: 0) {
                uid
                name
                avatar
                verify {
                    type
                    info
                }
                status
            }
            userCount(uid: 0) {
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
            userPunch(uid: 0) {
                isPunch
                punchallday
                punchday
                punchhighestday
            }
        }
    """
}