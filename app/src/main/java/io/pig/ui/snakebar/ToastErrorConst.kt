package io.pig.ui.snakebar

import io.pig.lkong.R

object ToastErrorConst {
    const val TOAST_FAILURE_SIGN = 17
    const val TOAST_FAILURE_USER_INFO = 19
    const val TOAST_FAILURE_FORUM_LIST = 20
    const val TOAST_FAILURE_RATE_POST = 55
    const val TOAST_FAILURE_SAVE_IMAGE_AS = 68
    fun errorCodeToStringRes(errorCode: Int): Int {
        return when (errorCode) {
            TOAST_FAILURE_SIGN -> R.string.toast_failure_sign
            TOAST_FAILURE_USER_INFO -> R.string.toast_failure_get_user_info
            TOAST_FAILURE_FORUM_LIST -> R.string.toast_failure_get_forum_list
            TOAST_FAILURE_RATE_POST -> R.string.toast_failure_rate_post
            TOAST_FAILURE_SAVE_IMAGE_AS -> R.string.toast_failure_save_image_as
            else -> R.string.toast_error_universal_try_again
        }
    }
}