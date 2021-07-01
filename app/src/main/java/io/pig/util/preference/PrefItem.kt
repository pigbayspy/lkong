package io.pig.util.preference

import android.content.SharedPreferences

/**
 * @author yinhang
 * @since 2021/7/1
 */
abstract class PrefItem<T>(
    protected val key: String,
    protected val default: T,
    protected val preference: SharedPreferences
) {
    abstract fun get(): T

    abstract fun set(value: T)
}