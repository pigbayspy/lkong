package io.pig.lkong.data.provider

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.DefaultDatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.pig.lkong.data.provider.cacahe.CacheObjectColumns

class LkongSQLiteOpenHelper(context: Context, errorHandler: DatabaseErrorHandler) :
    SQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_CACHE_OBJECT)
        db.execSQL(SQL_CREATE_INDEX_CACHE_OBJECT_CACHE_KEY)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // nothing to do
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        if (!db.isReadOnly) {
            setForeignKeyConstraintsEnabled(db)
        }
    }

    private fun setForeignKeyConstraintsEnabled(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    companion object {
        const val DATABASE_FILE_NAME = "lkong.db"

        const val DATABASE_VERSION = 1

        const val SQL_CREATE_TABLE_CACHE_OBJECT =
            """
            CREATE TABLE IF NOT EXISTS ${CacheObjectColumns.TABLE_NAME} 
            ( ${CacheObjectColumns.CACHE_ID} INTEGER PRIMARY KEY AUTOINCREMENT, 
            ${CacheObjectColumns.CACHE_KEY} TEXT NOT NULL, 
            ${CacheObjectColumns.CACHE_VALUE} TEXT NOT NULL, 
            ${CacheObjectColumns.CACHE_TIME_CREATE} INTEGER, 
            ${CacheObjectColumns.CACHE_TIME_EXPIRE} INTEGER,
            CONSTRAINT unique_cache_key UNIQUE (cache_key) ON CONFLICT REPLACE);    
            """

        const val SQL_CREATE_INDEX_CACHE_OBJECT_CACHE_KEY =
            """
            CREATE INDEX IDX_CACHE_OBJECT_CACHE_KEY 
            ON ${CacheObjectColumns.TABLE_NAME} 
            ( ${CacheObjectColumns.CACHE_KEY} );
            """

        private var instance: LkongSQLiteOpenHelper? = null

        fun getInstance(context: Context): LkongSQLiteOpenHelper {
            if (instance == null) {
                instance =
                    LkongSQLiteOpenHelper(
                        context,
                        DefaultDatabaseErrorHandler()
                    )
            }
            return instance!!
        }
    }
}