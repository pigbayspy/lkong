package io.pig.lkong.http.provider

import io.pig.lkong.account.LkongAuthObject
import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.cookie.CookieManager
import io.pig.lkong.http.cookie.impl.InMemoryCookieStore
import io.pig.lkong.http.spec.LkongSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @author yinhang
 * @since 2021/6/17
 */
object LkongServiceProvider {

    private val httpClient: OkHttpClient

    val lkongClient: LkongSpec

    val lkongCookie: CookieManager

    init {
        lkongCookie = InMemoryCookieStore()
        httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cookieJar(lkongCookie)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(RestApiConst.LKONG_HOST)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        lkongClient = retrofit.create(LkongSpec::class.java)
    }

    fun addAccount(auth: LkongAuthObject) {
        lkongCookie.clear()
        val cookies = listOf(auth.authCookie)
        lkongCookie.saveFromResponse(auth.authUrl, cookies)
    }
}