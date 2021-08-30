package io.pig.lkong.model

import android.text.DynamicLayout
import android.text.SpannableStringBuilder
import android.text.StaticLayout

/**
 * @author yinhang
 * @since 2021/6/29
 */
class PostDisplayModel(
    val urlSpanCount: Int,
    val importantSpans: List<Any>,
    val emoticonSpans: List<Any>,
    val textLayout: DynamicLayout,
    val authorLayout: StaticLayout,
    val spannableStringBuilder: SpannableStringBuilder
)