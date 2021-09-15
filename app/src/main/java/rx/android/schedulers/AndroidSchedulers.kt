package rx.android.schedulers

import android.os.Handler
import android.os.Looper
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor

/**
 * @author yinhang
 * @since 2021/5/16
 */
object AndroidSchedulers : Executor {

    private val mainScheduler: Scheduler = Schedulers.from(this)
    private val handler: Handler = Handler(Looper.myLooper()!!)

    fun mainThread(): Scheduler {
        return mainScheduler
    }

    override fun execute(command: Runnable) {
        handler.post(command)
    }
}