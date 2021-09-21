package io.pig.lkong.data.provider.cacahe

import android.net.Uri
import android.provider.BaseColumns
import io.pig.lkong.data.provider.ProviderConst

/**
 * Cache any kind of object here.
 */
object CacheObjectColumns : BaseColumns {
    const val TABLE_NAME = "cache_object"

    private val CONTENT_URI_NOTIFY =
        Uri.parse(ProviderConst.CONTENT_URI_BASE + "/" + TABLE_NAME)
            .buildUpon().appendQueryParameter("QUERY_NOTIFY", java.lang.Boolean.toString(true))
            .build()

    val CONTENT_URI = Uri.parse(ProviderConst.CONTENT_URI_BASE + "/" + TABLE_NAME)
        .buildUpon().appendQueryParameter("QUERY_NOTIFY", java.lang.Boolean.toString(false)).build()

    fun contentUri(authority: String): Uri {
        return Uri.parse("content://$authority/$TABLE_NAME")
            .buildUpon().appendQueryParameter("QUERY_NOTIFY", java.lang.Boolean.toString(false))
            .build()
    }

    fun contentUriNotify(authority: String): Uri {
        return Uri.parse("content://$authority/$TABLE_NAME")
            .buildUpon().appendQueryParameter("QUERY_NOTIFY", java.lang.Boolean.toString(true))
            .build()
    }

    /**
     * Primary key.
     */
    const val CACHE_ID = BaseColumns._ID

    /**
     * The key of cache object, unique and indexed.
     */
    const val CACHE_KEY = "cache_key"

    /**
     * The value of cache, could be simple String or Json String.
     */
    const val CACHE_VALUE = "cache_value"

    /**
     * The create time of cache, nullable.
     */
    const val CACHE_TIME_CREATE = "cache_time_create"

    /**
     * The expire time of cache, nullable.
     */
    const val CACHE_TIME_EXPIRE = "cache_time_expire"

    const val DEFAULT_ORDER = TABLE_NAME + "." + CACHE_ID
}