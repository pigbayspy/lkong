package io.pig.lkong.ui.adapter.base

import android.os.Parcelable
import java.io.Serializable

/**
 * @author yinhang
 * @since 2021/6/8
 */
interface SimpleCollectionItem : Serializable, Parcelable {

    /**
     * 获取排序的键
     */
    fun getSortKey(): Long
}