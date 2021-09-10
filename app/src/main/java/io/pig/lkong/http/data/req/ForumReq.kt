package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/7/10
 */
class ForumReq {
    val query = """
        query {
            ... on Query {
                forums {
                    name
                    fid
                    avatar
                    todayPostNum
                }
            }
        }
    """
}