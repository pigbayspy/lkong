package io.pig.lkong.http.data.req

class NewThreadReq(fid: Long, title: String, content: String) {
    val operationName = "operationName"

    val query = """
    mutation NewThread {
        postThread(fid: $fid, title: "$title", content: "$content") {
            tid
        }
    }
    """
}