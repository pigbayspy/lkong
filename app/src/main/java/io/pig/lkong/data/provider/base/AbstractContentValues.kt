package io.pig.lkong.data.provider.base

import android.content.ContentValues
import android.content.ContentResolver
import android.content.Context
import android.net.Uri

abstract class AbstractContentValues {
    protected val contentValues = ContentValues()

    /**
     * Returns the `uri` argument to pass to the `ContentResolver` methods.
     */
    abstract fun uri(): Uri

    /**
     * Returns the `ContentValues` wrapped by this object.
     */
    fun values(): ContentValues {
        return contentValues
    }

    /**
     * Inserts a row into a table using the values stored by this object.
     *
     * @param contentResolver The content resolver to use.
     */
    fun insert(contentResolver: ContentResolver): Uri? {
        return contentResolver.insert(uri(), values())
    }

    /**
     * Inserts a row into a table using the values stored by this object.
     *
     * @param context The context to use.
     */
    fun insert(context: Context): Uri? {
        return context.contentResolver.insert(uri(), values())
    }
}