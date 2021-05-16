package io.pig.lkong.navigation

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import io.pig.lkong.R
import io.pig.lkong.account.const.AccountConst
import io.pig.lkong.application.const.AppConst

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

    fun navigateToFaq(activity: Activity) {
        openUrl(AppConst.FAQ_URL, activity)
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

    private fun openUrl(url: String, context: Context) {
        val uri = Uri.parse(url)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        context.startActivity(intent)
    }
}