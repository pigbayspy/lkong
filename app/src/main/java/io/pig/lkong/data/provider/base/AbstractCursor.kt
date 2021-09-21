package io.pig.lkong.data.provider.base

import android.database.Cursor
import android.database.CursorWrapper
import java.util.*

abstract class AbstractCursor(cursor: Cursor) : CursorWrapper(cursor) {

    private val columnIndexes: HashMap<String, Int>

    abstract fun getId(): Long

    protected fun getCachedColumnIndexOrThrow(colName: String): Int {
        var index = columnIndexes[colName]
        if (index == null) {
            index = getColumnIndexOrThrow(colName)
            columnIndexes[colName] = index
        }
        return index
    }

    fun getStringOrNull(colName: String): String? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else getString(index)
    }

    fun getIntegerOrNull(colName: String): Int? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else getInt(index)
    }

    fun getLongOrNull(colName: String): Long? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else getLong(index)
    }

    fun getFloatOrNull(colName: String): Float? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else getFloat(index)
    }

    fun getDoubleOrNull(colName: String): Double? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else getDouble(index)
    }

    fun getBooleanOrNull(colName: String): Boolean? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else getInt(index) != 0
    }

    fun getDateOrNull(colName: String): Date? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else Date(getLong(index))
    }

    fun getBlobOrNull(colName: String): ByteArray? {
        val index = getCachedColumnIndexOrThrow(colName)
        return if (isNull(index)) {
            null
        } else getBlob(index)
    }

    init {
        columnIndexes = HashMap(cursor.columnCount * 4 / 3, .75f)
    }
}