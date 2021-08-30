package io.pig.lkong.http.data.req

class NoticeReq {
    val variables = emptyMap<String, Any>()

    val query = """
    {
        noticeCount {
            noticeCount
            fansCount
            rateCount
            atmeCount
            pmCount
        }
    }
    """
}