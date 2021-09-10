package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class HotThreadReq {
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
