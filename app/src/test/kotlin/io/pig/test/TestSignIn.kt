package io.pig.test

import io.pig.lkong.BuildConfig
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
        val userEmail = BuildConfig.lkongAccount
        val password = BuildConfig.lkongPassword
        val resp = LkongRepository.signIn(userEmail, password)
        assertNotNull(resp, "返回数据不为空")
        assertTrue(resp.name.isNotEmpty(), "登录失败")
    }
}