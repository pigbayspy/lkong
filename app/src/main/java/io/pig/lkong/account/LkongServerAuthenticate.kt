package io.pig.lkong.account

import io.pig.lkong.http.data.LkongAuthResp
import io.pig.lkong.http.data.LkongSignReq
import io.pig.lkong.http.source.LkongRepository

/**
 * @author yinhang
 * @since 2021/5/18
 */
class LkongServerAuthenticate {

    fun signIn(email: String, password: String): LkongAuthResp {
        val signInReq = LkongSignReq(email, password)
        return LkongRepository.signIn(signInReq)
    }
}