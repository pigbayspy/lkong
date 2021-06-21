package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/6/17
 */
data class LkongSignReq(
    val email: String,
    val password: String
) {
    val action = "login"
    val rememberMe = "on"
}