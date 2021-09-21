package io.pig.lkong.data.provider.cacahe

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import io.pig.lkong.data.provider.base.AbstractSelection

/**
 * Selection for the `cache_object` table.
 */
class CacheObjectSelection : AbstractSelection<CacheObjectSelection>() {
    override fun baseUri(): Uri {
        return CacheObjectColumns.CONTENT_URI
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A `CacheObjectCursor` object, which is positioned before the first entry, or null.
     */
    fun query(contentResolver: ContentResolver, projection: Array<String?>?): CacheObjectCursor? {
        val cursor = contentResolver.query(uri(), projection, sel(), args(), order()) ?: return null
        return CacheObjectCursor(cursor)
    }

    /**
     * Equivalent of calling `query(contentResolver, null)`.
     */
    fun query(contentResolver: ContentResolver): CacheObjectCursor? {
        return query(contentResolver, null)
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A `CacheObjectCursor` object, which is positioned before the first entry, or null.
     */
    fun query(context: Context, projection: Array<String?>?): CacheObjectCursor? {
        val cursor = context.contentResolver.query(uri(), projection, sel(), args(), order())
            ?: return null
        return CacheObjectCursor(cursor)
    }

    /**
     * Equivalent of calling `query(context, null)`.
     */
    fun query(context: Context): CacheObjectCursor? {
        return query(context, null)
    }

    fun id(vararg value: Long): CacheObjectSelection {
        addEquals("cache_object." + CacheObjectColumns.CACHE_ID, toObjectArray(*value))
        return this
    }

    fun orderById(desc: Boolean = false): CacheObjectSelection {
        orderBy("cache_object." + CacheObjectColumns.CACHE_ID, desc)
        return this
    }

    fun cacheKey(vararg value: String?): CacheObjectSelection {
        addEquals(CacheObjectColumns.CACHE_KEY, arrayOf(value))
        return this
    }
}