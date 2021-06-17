package io.pig.lkong.http.provider

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.cookie.impl.InMemoryCookieStore
import io.pig.lkong.http.spec.LkongSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


/**
 * @author yinhang
 * @since 2021/6/17
 */
object LkongServiceProvider {

    private val httpClient: OkHttpClient

    val lkongClient: LkongSpec

    init {
        val cookieJar = InMemoryCookieStore()
        httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(RestApiConst.BASE_URL)
            .client(httpClient)
            .build()
        lkongClient = retrofit.create(LkongSpec::class.java)
    }
}