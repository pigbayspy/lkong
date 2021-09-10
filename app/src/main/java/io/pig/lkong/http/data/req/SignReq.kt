package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/09/10
 */
class SignReq(email: String, password: String) {
    val query = """
    mutation {  
        login(email: "$email", password: "$password") {
            uid
            name
            avatar
        }
    }
    """
}