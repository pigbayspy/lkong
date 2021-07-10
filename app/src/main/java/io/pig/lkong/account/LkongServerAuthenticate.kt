package io.pig.lkong.account

import io.pig.lkong.http.data.LkongSignInResp
import io.pig.lkong.http.data.LkongSignReq
import io.pig.lkong.http.source.LkongRepository

/**
 * @author yinhang
 * @since 2021/5/18
 */
class LkongServerAuthenticate {

    suspend fun signIn(email: String, password: String): LkongSignInResp {
        return LkongRepository.signIn(email, password)
    }
}