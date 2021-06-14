package io.pig.lkong.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * @author yinhang
 * @since 2021/6/14
 */
object NetworkUtil {

    private var networkType = NetworkCapabilities.TRANSPORT_CELLULAR

    private var wifiConnect = false

    fun checkNetworkState(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val activeNetwork = cm.getNetworkCapabilities(network)
        if (activeNetwork != null) {
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                wifiConnect = true
            } else {
                wifiConnect = false
            }
        } else {
            wifiConnect = false
        }
    }

    fun isWifiConnect(): Boolean {
        return wifiConnect
    }
}