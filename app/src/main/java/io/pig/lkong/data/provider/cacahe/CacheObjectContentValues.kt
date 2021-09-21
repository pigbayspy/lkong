package io.pig.lkong.data.provider.cacahe

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import io.pig.lkong.data.provider.base.AbstractContentValues

/**
 * Content values wrapper for the `cache_object` table.
 */
class CacheObjectContentValues : AbstractContentValues() {
    override fun uri(): Uri {
        return CacheObjectColumns.CONTENT_URI
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be `null`).
     */
    fun update(contentResolver: ContentResolver, where: CacheObjectSelection?): Int {
        return contentResolver.update(
            uri(),
            values(),
            where?.sel(),
            where?.args()
        )
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param context The content resolver to use.
     * @param where The selection to use (can be `null`).
     */
    fun update(context: Context, where: CacheObjectSelection?): Int {
        return context.contentResolver.update(
            uri(),
            values(),
            where?.sel(),
            where?.args()
        )
    }

    /**
     * The key of cache object, unique and indexed.
     */
    fun putCacheKey(value: String): CacheObjectContentValues {
        contentValues.put(CacheObjectColumns.CACHE_KEY, value)
        return this
    }

    /**
     * The value of cache, could be simple String or Json String.
     */
    fun putCacheValue(value: String): CacheObjectContentValues {
        contentValues.put(CacheObjectColumns.CACHE_VALUE, value)
        return this
    }

    /**
     * The create time of cache, nullable.
     */
    fun putCacheTimeCreate(value: Long?): CacheObjectContentValues {
        contentValues.put(CacheObjectColumns.CACHE_TIME_CREATE, value)
        return this
    }

    fun putCacheTimeCreateNull(): CacheObjectContentValues {
        contentValues.putNull(CacheObjectColumns.CACHE_TIME_CREATE)
        return this
    }

    /**
     * The expire time of cache, nullable.
     */
    fun putCacheTimeExpire(value: Long?): CacheObjectContentValues {
        contentValues.put(CacheObjectColumns.CACHE_TIME_EXPIRE, value)
        return this
    }

    fun putCacheTimeExpireNull(): CacheObjectContentValues {
        contentValues.putNull(CacheObjectColumns.CACHE_TIME_EXPIRE)
        return this
    }
}