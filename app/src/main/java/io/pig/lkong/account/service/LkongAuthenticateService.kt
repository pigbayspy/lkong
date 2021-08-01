package io.pig.lkong.account.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.pig.lkong.account.LkongAuthenticator

class LkongAuthenticateService : Service() {

    override fun onBind(intent: Intent): IBinder {
        val lkongAuthenticate = LkongAuthenticator(this)
        return lkongAuthenticate.iBinder
    }
}