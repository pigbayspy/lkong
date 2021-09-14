package io.pig.lkong.http.data.req

class SystemNoticeReq(date: Long) {

    val query = """
    query {
        notice(date: $date) {
            data: notices {
                id
                uid
                action
                content
                authorid
                author
                dateline
                fromid
            }
            hasMore
            nextDate
        }
    }
    """
}