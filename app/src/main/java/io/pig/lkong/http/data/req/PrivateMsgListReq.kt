package io.pig.lkong.http.data.req

class PrivateMsgListReq(val date:Long) {

    val query = """
    query {
        lastUsersMessages(date: $date) {
            data: messages {
                user {
                    uid
                    name
                    avatar
                }
                lastTime
                newCount
                content
            }
            hasMore
            nextDate
        }
    }
    """
}