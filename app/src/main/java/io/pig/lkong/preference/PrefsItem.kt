package io.pig.lkong.preference

import android.content.SharedPreferences

/**
 * @author yinhang
 * @since 2021/5/16
 */
abstract class PrefsItem<T>(
    protected val key: String,
    protected val defaultValue: T,
    protected val preferences: SharedPreferences
) {
    /**
     * 获取数据
     */
    abstract fun get(): T

    /**
     * 设置数据
     */
    abstract fun set(value: T)
}