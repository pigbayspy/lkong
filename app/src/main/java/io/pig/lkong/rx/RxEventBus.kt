package io.pig.lkong.rx

import io.pig.lkong.rx.event.AbstractEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

/**
 * @author yinhang
 * @since 2021/5/16
 */
object RxEventBus {

    private val instance: Subject<AbstractEvent>

    init {
        val subject: Subject<AbstractEvent> = PublishSubject.create()
        instance = subject.toSerialized()
    }

    fun sendEvent(event: AbstractEvent?) {
        instance.onNext(event)
    }

    fun toObservable(): Observable<AbstractEvent> {
        return instance
    }
}