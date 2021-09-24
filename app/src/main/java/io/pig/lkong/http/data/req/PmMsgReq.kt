package io.pig.lkong.http.data.req

class PmMsgReq(userId: Long, date: Long) {

    val query = """
    query {
        userMessages(touid: $userId, date: $date) {
            data: messages {
                content
                dateline
                id
                uid
            }
            hasMore
            nextDate
        }
    } 
    """
}