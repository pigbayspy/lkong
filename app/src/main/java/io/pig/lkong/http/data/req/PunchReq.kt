package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class PunchReq {
    val query = """
    mutation {
        punch {
            uid
            punchday
            isPunch
            punchhighestday
            punchallday
        }
    }    
    """
}