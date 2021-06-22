package io.pig.test

import io.pig.lkong.http.data.LkongSignReq
import io.pig.lkong.http.source.LkongRepository
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestRestApi {

    @Test
    fun testSignIn() {
        val userEmail = "469608745@qq.com"
        val password = "WODESHENGRI19970720"
        val signInReq = LkongSignReq(userEmail, password)
        val resp = LkongRepository.signIn(signInReq)
        assertNotNull(resp, "返回数据不为空")
        assertTrue(resp.success, "登录失败")
        assertTrue(resp.name.isNotEmpty(), "登录失败")
        assertTrue(resp.yousuu.isNotEmpty(), "登录失败")
        assertTrue(resp.authCookie.isNotEmpty(), "登录失败")
    }
}