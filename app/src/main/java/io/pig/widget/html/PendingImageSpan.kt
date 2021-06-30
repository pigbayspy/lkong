package io.pig.widget.html

/**
 * @author yinhang
 * @since 2021/6/30
 */
interface PendingImageSpan {
    fun loadImage(container: ImageSpanContainer)
    fun loadImage(container: ImageSpanContainer, newMaxWidth: Int, backgroundColor: Int)
}