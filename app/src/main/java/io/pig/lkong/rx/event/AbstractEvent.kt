package io.pig.lkong.rx.event

/**
 * @author yinhang
 * @since 2021/5/16
 */
abstract class AbstractEvent {
    /**
     * 是否被处理
     */
    private var handled = false

    fun isHandled(): Boolean {
        return handled
    }

    fun setHandled(isHandled: Boolean) {
        this.handled = isHandled
    }

}