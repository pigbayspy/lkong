package io.pig.lkong.http.const

import okhttp3.HttpUrl.Companion.toHttpUrl

/**
 * @author yinhang
 * @since 2021/5/24
 */
object RestApiConst {

    const val LKONG_HOST = "http://lkong.cn"

    val Lkong_URl = LKONG_HOST.toHttpUrl()

    const val SIGN_URL = "/index.php?mod=login"

    const val GET_FAVORITE_URL = "/index.php?mod=data&sars=my/favorite"
}