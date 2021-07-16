package io.pig.lkong.http.data.req

/**
 * @author yinhang
 * @since 2021/7/10
 */
class SignReq(email: String, password: String) {

    val operationName = "Login"

    val variables = mapOf("email" to email, "password" to password)

    val query = """
        mutation Login(${'$'}email: String!, ${'$'}password: String!) {  
            login(email: ${'$'}email, password: ${'$'}password) {
                uid
                name
            }
        }
        """
}