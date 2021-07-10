package io.pig.test

import io.pig.lkong.http.source.LkongRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author yinhang
 * @since 2021/6/24
 */
class TestSignIn {

    @Test
    fun testSignIn(): Unit = runBlocking {
        val userEmail = "469608745@qq.com"
        val password = "WODESHENGRI19970720"
        val resp = LkongRepository.sign(userEmail, password)
        assertNotNull(resp, "返回数据不为空")
        val respData = resp.data
        assertNotNull(respData, "登录失败")
        val signResp = respData.login
        assertNotNull(signResp, "登录失败")
        assertTrue(signResp.name.isNotEmpty(), "登录失败")
    }
}