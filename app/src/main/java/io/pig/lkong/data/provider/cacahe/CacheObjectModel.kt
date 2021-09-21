package io.pig.lkong.data.provider.cacahe

import io.pig.lkong.data.provider.base.BaseModel

/**
 * Cache any kind of object here.
 */
interface CacheObjectModel : BaseModel {
    /**
     * The key of cache object, unique and indexed.
     * Cannot be `null`.
     */
    fun cacheKey(): String?

    /**
     * The value of cache, could be simple String or Json String.
     * Cannot be `null`.
     */
    fun cacheValue(): String?

    /**
     * The create time of cache, nullable.
     * Can be `null`.
     */
    fun cacheTimeCreate(): Long?

    /**
     * The expire time of cache, nullable.
     * Can be `null`.
     */
    fun cacheTimeExpire(): Long?
}