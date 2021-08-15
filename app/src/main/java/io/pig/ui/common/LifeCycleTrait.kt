package io.pig.ui.common

import androidx.appcompat.app.AppCompatActivity

/**
 * @author yinhang
 * @since 2021/8/15
 */
fun AppCompatActivity.isActivityDestroyed(): Boolean {
    return isDestroyed || isFinishing
}