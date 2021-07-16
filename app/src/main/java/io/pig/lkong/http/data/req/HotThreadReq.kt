package io.pig.lkong.http.data.req

/**
 *
 */
class HotThreadReq {

    val operationName = "ViewIndex"

    val variables = emptyMap<Any, Any>()

    val query = """
    query ViewIndex {
        ...indexConfigComponent
    }

    fragment indexConfigComponent on Query {
        hots: threadsFragment(fid: 0, type: "hot") {
            tid
            title
        }
    }
    """
}
