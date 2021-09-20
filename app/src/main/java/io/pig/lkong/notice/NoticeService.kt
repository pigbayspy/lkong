package io.pig.lkong.notice

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NoticeService : Service() {

    companion object {
        val lock = Object()
        var syncAdapter: NoticeSyncAdapter? = null
    }

    override fun onCreate() {
        super.onCreate()
        synchronized(lock) {
            if (syncAdapter == null) {
                syncAdapter = NoticeSyncAdapter(applicationContext, true)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return syncAdapter!!.syncAdapterBinder
    }
}