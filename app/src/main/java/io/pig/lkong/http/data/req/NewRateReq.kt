package io.pig.lkong.http.data.req

class NewRateReq(num:Int,pid:String, reason:String, tid:Long) {
    val operationName = "Rate"
    val query = """
    mutation Rate {
        rate(tid: $tid, pid: "$pid", num: $num, reason: "$reason") {
            id
            user {
                uid
                name
                avatar
            }
            pid
            num
            reason
            dateline
        }
    }
    """
}