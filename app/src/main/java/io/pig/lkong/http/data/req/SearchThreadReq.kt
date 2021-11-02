package io.pig.lkong.http.data.req

class SearchThreadReq(text: String, sort: String = "", searchAfter: String = "") {

    val query = """
    query {
        searchThreads(text: "$text", sort: "$sort", searchAfter: "$searchAfter") {
            searchAfter
            hasMore
            threads {
                tid
                title
                forum {
                    fid
                    name
                }
                author {
                    name
                    uid
                }
                dateline
                replies
                views
            }
        }
    }
    """
}