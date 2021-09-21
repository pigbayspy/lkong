package io.pig.lkong.data.provider.cacahe

import android.database.Cursor
import io.pig.lkong.data.provider.base.AbstractCursor

/**
 * Cursor wrapper for the `cache_object` table.
 */
class CacheObjectCursor(cursor: Cursor) : AbstractCursor(
    cursor
), CacheObjectModel {
    /**
     * Primary key.
     */
    override fun getId(): Long {
        return getLongOrNull(CacheObjectColumns.CACHE_ID)
            ?: throw NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition")
    }

    /**
     * The key of cache object, unique and indexed.
     * Cannot be `null`.
     */
    override fun cacheKey(): String {
        return getStringOrNull(CacheObjectColumns.CACHE_KEY)
            ?: throw NullPointerException("The value of 'cache_key' in the database was null, which is not allowed according to the model definition")
    }

    /**
     * The value of cache, could be simple String or Json String.
     * Cannot be `null`.
     */
    override fun cacheValue(): String {
        return getStringOrNull(CacheObjectColumns.CACHE_VALUE)
            ?: throw NullPointerException("The value of 'cache_value' in the database was null, which is not allowed according to the model definition")
    }

    /**
     * The create time of cache, nullable.
     * Can be `null`.
     */

    override fun cacheTimeCreate(): Long? {
        return getLongOrNull(CacheObjectColumns.CACHE_TIME_CREATE)
    }

    /**
     * The expire time of cache, nullable.
     * Can be `null`.
     */
    override fun cacheTimeExpire(): Long? {
        return getLongOrNull(CacheObjectColumns.CACHE_TIME_EXPIRE)
    }
}