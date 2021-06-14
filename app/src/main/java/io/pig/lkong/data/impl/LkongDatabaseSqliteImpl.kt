package io.pig.lkong.data.impl

import android.content.ContentResolver
import android.content.Context
import com.google.gson.Gson
import io.pig.lkong.data.LkongDatabase

/**
 * @author yinhang
 * @since 2021/6/14
 */
class LkongDatabaseSqliteImpl : LkongDatabase {

    private val gson: Gson
    private val contentResolver: ContentResolver

    constructor (context: Context) {
        contentResolver = context.contentResolver
        gson = Gson()
    }

    override fun init() {
    }

    override fun close() {

    }

}