package io.pig.lkong.model

/**
 * @author yinhang
 * @since 2021/8/8
 */
data class TimelineContentModel(
    val type: String,
    val children: List<Map<String, Any>>
)