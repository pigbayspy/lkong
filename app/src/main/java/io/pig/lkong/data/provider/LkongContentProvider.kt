package io.pig.lkong.data.provider

import android.content.UriMatcher
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import io.pig.lkong.data.provider.base.BaseContentProvider
import io.pig.lkong.data.provider.base.QueryParams
import io.pig.lkong.data.provider.cacahe.CacheObjectColumns

open class LkongContentProvider : BaseContentProvider() {

    companion object {
        const val URI_TYPE_CACHE_OBJECT = 0
        const val URI_TYPE_CACHE_OBJECT_ID = 1

        private const val TYPE_CURSOR_ITEM = "vnd.android.cursor.item/"
        private const val TYPE_CURSOR_DIR = "vnd.android.cursor.dir/"
        protected val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(
                ProviderConst.AUTHORITY,
                CacheObjectColumns.TABLE_NAME,
                URI_TYPE_CACHE_OBJECT
            )
            uriMatcher.addURI(
                ProviderConst.AUTHORITY,
                CacheObjectColumns.TABLE_NAME + "/#",
                URI_TYPE_CACHE_OBJECT_ID
            )
        }
    }

    override fun createSqLiteOpenHelper(): SQLiteOpenHelper {
        return LkongSQLiteOpenHelper.getInstance(context!!)
    }

    override fun getType(uri: Uri): String? {
        val match = getMatcher().match(uri)
        when (match) {
            URI_TYPE_CACHE_OBJECT -> return TYPE_CURSOR_DIR + CacheObjectColumns.TABLE_NAME
            URI_TYPE_CACHE_OBJECT_ID -> return TYPE_CURSOR_ITEM + CacheObjectColumns.TABLE_NAME
        }
        return null
    }

    override fun getQueryParams(
        uri: Uri,
        selection: String?,
        projection: Array<String>?
    ): QueryParams {
        val res: QueryParams
        var id: String? = null
        val matchedId = getMatcher().match(uri)
        when (matchedId) {
            URI_TYPE_CACHE_OBJECT, URI_TYPE_CACHE_OBJECT_ID -> {
                res = QueryParams(
                    table = CacheObjectColumns.TABLE_NAME,
                    idColumn = CacheObjectColumns.CACHE_ID,
                    tablesWithJoins = CacheObjectColumns.TABLE_NAME,
                    orderBy = CacheObjectColumns.DEFAULT_ORDER
                )
            }
            else -> throw IllegalArgumentException("The uri '$uri' is not supported by this ContentProvider")
        }
        when (matchedId) {
            URI_TYPE_CACHE_OBJECT_ID -> id = uri.lastPathSegment
        }
        if (id != null) {
            if (selection != null) {
                res.selection =
                    res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")"
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id
            }
        } else {
            res.selection = selection!!
        }
        return res
    }

    override fun getMatcher(): UriMatcher {
        return uriMatcher
    }
}