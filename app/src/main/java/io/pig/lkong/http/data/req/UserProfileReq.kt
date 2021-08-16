package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserProfileReq(uid: Long) {

    val operationName = "ViewIndex"

    val variables = mapOf("uid" to uid)

    val query = """
        query ViewIndex(${'$'}uid: Int!) {  
            ...indexConfigComponent
        }
        fragment indexConfigComponent on Query {
            user(uid: ${'$'}uid) {
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