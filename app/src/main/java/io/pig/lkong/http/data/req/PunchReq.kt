package io.pig.lkong.http.data.req

class PunchReq {

    private val variables = emptyMap<String, Any>()

    private val query =
    """
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