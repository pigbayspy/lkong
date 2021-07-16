package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/7/10
 */
class ForumReq {
    val operationName = "GetMe"
    val query = """
        query GetMe {
            ... on Query {
                commonNavbars {
                    type
                    name
                    fid
                    num
                    link
                }
            }
        }
    """
}