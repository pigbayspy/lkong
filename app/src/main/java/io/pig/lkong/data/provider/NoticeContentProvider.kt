package io.pig.lkong.data.provider

import android.content.UriMatcher
import io.pig.lkong.data.provider.cacahe.CacheObjectColumns

class NoticeContentProvider : LkongContentProvider() {
    companion object {
        private const val CHECK_NOTICE_AUTHORITY = "io.pig.lkong.data.provider.NOTICE"
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(
                CHECK_NOTICE_AUTHORITY,
                CacheObjectColumns.TABLE_NAME,
                URI_TYPE_CACHE_OBJECT
            )
            uriMatcher.addURI(
                CHECK_NOTICE_AUTHORITY,
                CacheObjectColumns.TABLE_NAME + "/#",
                URI_TYPE_CACHE_OBJECT_ID
            )
        }
    }

    override fun getMatcher(): UriMatcher {
        return uriMatcher
    }
}