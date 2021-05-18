package io.pig.lkong.account

import io.pig.lkong.http.data.LkongAuthResp
import io.pig.lkong.http.request.impl.SignInRequest

/**
 * @author yinhang
 * @since 2021/5/18
 */
class LkongServerAuthenticate {

    fun signIn(email: String, password: String): LkongAuthResp? {
        val signInRequest = SignInRequest(email, password)
        return signInRequest.execute()
    }
}