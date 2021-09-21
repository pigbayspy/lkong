package io.pig.lkong.data.provider.base

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import io.pig.lkong.data.provider.base.BaseContentProvider.Companion.groupBy
import io.pig.lkong.data.provider.base.BaseContentProvider.Companion.having
import io.pig.lkong.data.provider.base.BaseContentProvider.Companion.limit
import io.pig.lkong.data.provider.base.BaseContentProvider.Companion.notify
import io.pig.lkong.data.provider.base.BaseContentProvider.Companion.offset
import java.util.*

abstract class AbstractSelection<T> {

    private val selection = StringBuilder()
    private val selectionArgs: MutableList<String> = ArrayList(5)
    private val orderBy = StringBuilder()

    private var notify: Boolean? = null

    private var groupBy: String? = null

    private var having: String? = null

    private var limit: Int? = null

    private var offset: Int? = null

    protected fun addEquals(column: String, value: Array<Any?>?) {
        selection.append(column)
        if (value == null) {
            // Single null value
            selection.append(IS_NULL)
        } else if (value.size > 1) {
            // Multiple values ('in' clause)
            selection.append(IN)
            for (i in value.indices) {
                selection.append("?")
                if (i < value.size - 1) {
                    selection.append(COMMA)
                }
                selectionArgs.add(valueOf(value[i]))
            }
            selection.append(PAREN_CLOSE)
        } else {
            // Single value
            if (value[0] == null) {
                // Single null value
                selection.append(IS_NULL)
            } else {
                // Single not null value
                selection.append(EQ)
                selectionArgs.add(valueOf(value[0]))
            }
        }
    }

    protected fun addNotEquals(column: String, value: Array<Any?>?) {
        selection.append(column)
        if (value == null) {
            // Single null value
            selection.append(IS_NOT_NULL)
        } else if (value.size > 1) {
            // Multiple values ('in' clause)
            selection.append(NOT_IN)
            for (i in value.indices) {
                selection.append("?")
                if (i < value.size - 1) {
                    selection.append(COMMA)
                }
                selectionArgs.add(valueOf(value[i]))
            }
            selection.append(PAREN_CLOSE)
        } else {
            // Single value
            if (value[0] == null) {
                // Single null value
                selection.append(IS_NOT_NULL)
            } else {
                // Single not null value
                selection.append(NOT_EQ)
                selectionArgs.add(valueOf(value[0]))
            }
        }
    }

    protected fun addLike(column: String, values: Array<String>) {
        selection.append(PAREN_OPEN)
        for (i in values.indices) {
            selection.append(column)
            selection.append(LIKE)
            selectionArgs.add(values[i])
            if (i < values.size - 1) {
                selection.append(OR)
            }
        }
        selection.append(PAREN_CLOSE)
    }

    protected fun addContains(column: String, values: Array<String>) {
        selection.append(PAREN_OPEN)
        for (i in values.indices) {
            selection.append(column)
            selection.append(CONTAINS)
            selectionArgs.add(values[i])
            if (i < values.size - 1) {
                selection.append(OR)
            }
        }
        selection.append(PAREN_CLOSE)
    }

    protected fun addStartsWith(column: String, values: Array<String>) {
        selection.append(PAREN_OPEN)
        for (i in values.indices) {
            selection.append(column)
            selection.append(STARTS)
            selectionArgs.add(values[i])
            if (i < values.size - 1) {
                selection.append(OR)
            }
        }
        selection.append(PAREN_CLOSE)
    }

    protected fun addEndsWith(column: String, values: Array<String>) {
        selection.append(PAREN_OPEN)
        for (i in values.indices) {
            selection.append(column)
            selection.append(ENDS)
            selectionArgs.add(values[i])
            if (i < values.size - 1) {
                selection.append(OR)
            }
        }
        selection.append(PAREN_CLOSE)
    }

    protected fun addGreaterThan(column: String, value: Any?) {
        selection.append(column)
        selection.append(GT)
        selectionArgs.add(valueOf(value))
    }

    protected fun addGreaterThanOrEquals(column: String, value: Any?) {
        selection.append(column)
        selection.append(GT_EQ)
        selectionArgs.add(valueOf(value))
    }

    protected fun addLessThan(column: String, value: Any?) {
        selection.append(column)
        selection.append(LT)
        selectionArgs.add(valueOf(value))
    }

    protected fun addLessThanOrEquals(column: String, value: Any?) {
        selection.append(column)
        selection.append(LT_EQ)
        selectionArgs.add(valueOf(value))
    }

    fun addRaw(raw: String, vararg args: Any?) {
        selection.append(" ")
        selection.append(raw)
        selection.append(" ")
        for (arg in args) {
            selectionArgs.add(valueOf(arg))
        }
    }

    private fun valueOf(obj: Any?): String {
        return when (obj) {
            is Date -> {
                obj.time.toString()
            }
            is Boolean -> {
                if (obj) "1" else "0"
            }
            is Enum<*> -> {
                obj.ordinal.toString()
            }
            else -> obj.toString()
        }
    }

    fun openParen(): AbstractSelection<T> {
        selection.append(PAREN_OPEN)
        return this
    }

    fun closeParen(): AbstractSelection<T> {
        selection.append(PAREN_CLOSE)
        return this
    }

    fun and(): AbstractSelection<T> {
        selection.append(AND)
        return this
    }

    fun or(): AbstractSelection<T> {
        selection.append(OR)
        return this
    }

    protected fun toObjectArray(vararg array: Int): Array<Any> {
        return arrayOf(array)
    }

    protected fun toObjectArray(vararg array: Long): Array<Any?> {
        return arrayOf(array)
    }

    protected fun toObjectArray(vararg array: Float): Array<Any> {
        return arrayOf(array)
    }

    protected fun toObjectArray(vararg array: Double): Array<Any> {
        return arrayOf(array)
    }

    protected fun toObjectArray(value: Boolean): Array<Any> {
        return arrayOf(value)
    }

    /**
     * Returns the selection produced by this object.
     */
    fun sel(): String {
        return selection.toString()
    }

    /**
     * Returns the selection arguments produced by this object.
     */
    fun args(): Array<String>? {
        return if (selectionArgs.isEmpty()) {
            null
        } else {
            selectionArgs.toTypedArray()
        }
    }

    /**
     * Returns the order string produced by this object.
     */
    fun order(): String? {
        return if (orderBy.isNotEmpty()) {
            orderBy.toString()
        } else null
    }

    /**
     * Returns the `uri` argument to pass to the `ContentResolver` methods.
     */
    fun uri(): Uri {
        var uri = baseUri()
        if (notify != null) {
            uri = notify(uri, notify!!)
        }
        if (groupBy != null) {
            uri = groupBy(uri, groupBy)
        }
        if (having != null) {
            uri = having(uri, having)
        }
        if (limit != null) {
            uri = limit(uri, limit.toString())
        }
        if (offset != null) {
            uri = offset(uri, offset.toString())
        }
        return uri
    }

    protected abstract fun baseUri(): Uri

    /**
     * Deletes row(s) specified by this selection.
     *
     * @param contentResolver The content resolver to use.
     * @return The number of rows deleted.
     */
    fun delete(contentResolver: ContentResolver): Int {
        return contentResolver.delete(uri(), sel(), args())
    }

    /**
     * Deletes row(s) specified by this selection.
     *
     * @param context The context to use.
     * @return The number of rows deleted.
     */
    fun delete(context: Context): Int {
        return context.contentResolver.delete(uri(), sel(), args())
    }

    fun notify(notify: Boolean): AbstractSelection<T> {
        this.notify = notify
        return this
    }

    fun groupBy(groupBy: String?): AbstractSelection<T> {
        this.groupBy = groupBy
        return this
    }

    fun having(having: String?): AbstractSelection<T> {
        this.having = having
        return this
    }

    fun limit(limit: Int): AbstractSelection<T> {
        this.limit = limit
        return this
    }

    fun offset(offset: Int): AbstractSelection<T> {
        this.offset = offset
        return this
    }

    fun orderBy(order: String, desc: Boolean = false): AbstractSelection<T> {
        if (orderBy.isNotEmpty()) {
            orderBy.append(COMMA)
        }
        orderBy.append(order)
        if (desc) {
            orderBy.append(DESC)
        }
        return this
    }

    fun orderBy(vararg orders: String): AbstractSelection<T> {
        for (order in orders) {
            orderBy(order, false)
        }
        return this
    }

    fun count(resolver: ContentResolver): Int {
        val cursor = resolver.query(
            uri(),
            arrayOf(COUNT),
            sel(),
            args(),
            null
        )
            ?: return 0
        return cursor.use {
            if (it.moveToFirst()) {
                it.getInt(0)
            } else 0
        }
    }

    companion object {
        private const val EQ = "=?"
        private const val PAREN_OPEN = "("
        private const val PAREN_CLOSE = ")"
        private const val AND = " AND "
        private const val OR = " OR "
        private const val IS_NULL = " IS NULL"
        private const val IS_NOT_NULL = " IS NOT NULL"
        private const val IN = " IN ("
        private const val NOT_IN = " NOT IN ("
        private const val COMMA = ","
        private const val GT = ">?"
        private const val LT = "<?"
        private const val GT_EQ = ">=?"
        private const val LT_EQ = "<=?"
        private const val NOT_EQ = "<>?"
        private const val LIKE = " LIKE ?"
        private const val CONTAINS = " LIKE '%' || ? || '%'"
        private const val STARTS = " LIKE ? || '%'"
        private const val ENDS = " LIKE '%' || ?"
        private const val COUNT = "COUNT(*)"
        private const val DESC = " DESC"
    }
}