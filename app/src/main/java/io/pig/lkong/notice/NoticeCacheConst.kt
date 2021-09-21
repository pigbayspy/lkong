package io.pig.lkong.notice


object NoticeCacheConst {
    private const val CACHE_KEY_PUNCH_RESULT = "cache_punch_result"
    private const val CACHE_KEY_NOTIFICATION_COUNT = "cache_notification_count"

    fun generatePunchResultKey(uid: Long): String {
        return "$CACHE_KEY_PUNCH_RESULT|||$uid"
    }

    fun generateNoticeCountKey(uid: Long): String {
        return "$CACHE_KEY_NOTIFICATION_COUNT|||$uid"
    }
}