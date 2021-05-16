package io.pig.lkong.navigation

import android.accounts.AccountManager
import android.app.Activity
import android.util.Log
import io.pig.lkong.R
import io.pig.lkong.account.const.AccountConst

/**
 * @author yinhang
 * @since 2021/5/16
 */
object AppNavigation {

    private const val TAG = "AppNavigation"

    fun navigateToSignInActivity(activity: Activity) {
        addNewAccount(
            activity,
            activity.resources.getString(R.string.account_type),
            AccountConst.AT_TYPE_FULL_ACCESS
        )
    }

    private fun addNewAccount(activity: Activity, accountType: String, authTokenType: String) {
        val accountManager = AccountManager.get(activity)
        accountManager.addAccount(
            accountType, authTokenType, null, null, activity,
            { future ->
                try {
                    val bnd = future.result
                    Log.d(TAG, "add new account bundle is $bnd")
                } catch (e: Exception) {
                    Log.e(TAG, "add new account error: ${e.message}")
                }
            }, null
        )
    }
}