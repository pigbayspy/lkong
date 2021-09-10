package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class NoticeReq {
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