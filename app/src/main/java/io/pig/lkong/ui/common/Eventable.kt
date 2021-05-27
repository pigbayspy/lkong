package io.pig.lkong.ui.common

import io.pig.lkong.rx.event.AbstractEvent

/**
 * @author yinhang
 * @since 2021/5/27
 */
interface Eventable {

    /**
     * 处理事件
     */
    fun onEvent(event: AbstractEvent)
}