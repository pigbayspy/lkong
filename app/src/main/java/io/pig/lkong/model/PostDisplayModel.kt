package io.pig.lkong.model

import android.text.DynamicLayout
import android.text.StaticLayout

/**
 * @author yinhang
 * @since 2021/6/29
 */
class PostDisplayModel(
    val urlSpanCount: Int,
    val importantSpans: List<Any>,
    val textLayout: DynamicLayout? = null,
    val authorLayout: StaticLayout? = null
)