package io.pig.test

import io.pig.lkong.http.data.LkongSignReq
import io.pig.lkong.http.source.LkongRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class TestRestApi {

    @Before
    fun signIn() {
        val userEmail = "469608745@qq.com"
        val password = "WODESHENGRI19970720"
        val signInReq = LkongSignReq(userEmail, password)
        LkongRepository.signIn(signInReq)
    }

    @Test
    fun testGetFavorite(): Unit = runBlocking {
        val result = LkongRepository.getFavoriteThread()
        assertNotNull(result, "get favorite not null")
        assertNotNull(result.data, "get favorite threads not null")
    }

    @Test
    fun testGetHot(): Unit = runBlocking {
        val result = LkongRepository.getHot()
        assertNotNull(result, "get hot not null")
        assertNotNull(result.thread, "get hot threads not null")
    }
}