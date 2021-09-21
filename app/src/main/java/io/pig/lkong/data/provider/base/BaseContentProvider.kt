package io.pig.lkong.data.provider.base

import android.content.ContentProvider
import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import android.text.TextUtils
import java.util.*

abstract class BaseContentProvider : ContentProvider() {
    protected abstract fun getQueryParams(
        uri: Uri,
        selection: String?,
        projection: Array<String>?
    ): QueryParams

    protected abstract fun createSqLiteOpenHelper(): SQLiteOpenHelper

    protected abstract fun getMatcher(): UriMatcher

    protected lateinit var sqLiteOpenHelper: SQLiteOpenHelper

    override fun onCreate(): Boolean {
        sqLiteOpenHelper = createSqLiteOpenHelper()
        return false
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val table = uri.lastPathSegment
        val rowId = sqLiteOpenHelper.writableDatabase.insertOrThrow(table, null, values)
        if (rowId == -1L) return null
        var notify: String
        if (uri.getQueryParameter(QUERY_NOTIFY)
                .also { notify = it!! } == null || "true" == notify
        ) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return uri.buildUpon().appendEncodedPath(rowId.toString()).build()
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        val table = uri.lastPathSegment
        val db = sqLiteOpenHelper.writableDatabase
        var res = 0
        db.beginTransaction()
        try {
            for (v in values) {
                val id = db.insert(table, null, v)
                db.yieldIfContendedSafely()
                if (id != -1L) {
                    res++
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        var notify: String
        if (res != 0 && (uri.getQueryParameter(QUERY_NOTIFY)
                .also { notify = it!! } == null || "true" == notify)
        ) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return res
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val (table, _, _, selection1) = getQueryParams(uri, selection, null)
        val res =
            sqLiteOpenHelper.writableDatabase.update(table, values, selection1, selectionArgs)
        var notify: String
        if (res != 0 && (uri.getQueryParameter(QUERY_NOTIFY)
                .also { notify = it!! } == null || "true" == notify)
        ) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return res
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val (table, _, _, selection1) = getQueryParams(uri, selection, null)
        val res = sqLiteOpenHelper.writableDatabase.delete(table, selection1, selectionArgs)
        var notify: String
        if (res != 0 && (uri.getQueryParameter(QUERY_NOTIFY)
                .also { notify = it!! } == null || "true" == notify)
        ) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return res
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val groupBy = uri.getQueryParameter(QUERY_GROUP_BY)
        val having = uri.getQueryParameter(QUERY_HAVING)
        val limit = uri.getQueryParameter(QUERY_LIMIT)
        val offset = uri.getQueryParameter(QUERY_OFFSET)
        val limitString = if (TextUtils.isEmpty(offset)) limit else "$offset,$limit"
        val (table, tablesWithJoins, idColumn, selection1, orderBy) = getQueryParams(
            uri,
            selection,
            projection
        )
        val newProjection = ensureIdIsFullyQualified(projection, table, idColumn)
        val res = sqLiteOpenHelper.readableDatabase.query(
            tablesWithJoins, newProjection, selection1, selectionArgs, groupBy,
            having, sortOrder ?: orderBy, limitString
        )
        res.setNotificationUri(context!!.contentResolver, uri)
        return res
    }

    private fun ensureIdIsFullyQualified(
        projection: Array<String>?,
        tableName: String,
        idColumn: String
    ): Array<String?>? {
        if (projection == null) {
            return null
        }
        val res = arrayOfNulls<String>(projection.size)
        for (i in projection.indices) {
            if (projection[i] == idColumn) {
                res[i] = tableName + "." + idColumn + " AS " + BaseColumns._ID
            } else {
                res[i] = projection[i]
            }
        }
        return res
    }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>): Array<ContentProviderResult?> {
        val urisToNotify = HashSet<Uri>(operations.size)
        for (operation in operations) {
            urisToNotify.add(operation.uri)
        }
        val db = sqLiteOpenHelper.writableDatabase
        db.beginTransaction()
        return try {
            val numOperations = operations.size
            val results = arrayOfNulls<ContentProviderResult>(numOperations)
            for ((i, operation) in operations.withIndex()) {
                results[i] = operation.apply(this, results, i)
                if (operation.isYieldAllowed) {
                    db.yieldIfContendedSafely()
                }
            }
            db.setTransactionSuccessful()
            for (uri in urisToNotify) {
                context!!.contentResolver.notifyChange(uri, null)
            }
            results
        } finally {
            db.endTransaction()
        }
    }

    companion object {
        const val QUERY_NOTIFY = "QUERY_NOTIFY"
        const val QUERY_GROUP_BY = "QUERY_GROUP_BY"
        const val QUERY_HAVING = "QUERY_HAVING"
        const val QUERY_LIMIT = "QUERY_LIMIT"
        const val QUERY_OFFSET = "QUERY_OFFSET"

        @JvmStatic
        fun notify(uri: Uri, notify: Boolean): Uri {
            return uri.buildUpon().appendQueryParameter(QUERY_NOTIFY, notify.toString()).build()
        }

        @JvmStatic
        fun groupBy(uri: Uri, groupBy: String?): Uri {
            return uri.buildUpon().appendQueryParameter(QUERY_GROUP_BY, groupBy).build()
        }

        @JvmStatic
        fun having(uri: Uri, having: String?): Uri {
            return uri.buildUpon().appendQueryParameter(QUERY_HAVING, having).build()
        }

        @JvmStatic
        fun limit(uri: Uri, limit: String?): Uri {
            return uri.buildUpon().appendQueryParameter(QUERY_LIMIT, limit).build()
        }

        @JvmStatic
        fun offset(uri: Uri, limit: String?): Uri {
            return uri.buildUpon().appendQueryParameter(QUERY_OFFSET, limit).build()
        }
    }
}