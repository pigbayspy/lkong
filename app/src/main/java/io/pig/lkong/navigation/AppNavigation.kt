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
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.ui.post.list.PostListActivity

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

    fun openActivityForUserProfile(context: Activity, startingLocation: IntArray, uid: Long) {
        // Todo
        // UserProfileActivity.startUserProfileFromLocation(context, startingLocation, uid)
    }

    fun openActivityForPostListByThreadId(context: Context, threadId: Long) {
        openActivityForPostListByThreadId(context, threadId, 1)
    }

    private fun openActivityForPostListByThreadId(context: Context, threadId: Long, page: Int) {
        val intent = Intent(context, PostListActivity::class.java)
        intent.putExtra(DataContract.BUNDLE_THREAD_ID, threadId)
        intent.putExtra(DataContract.BUNDLE_THREAD_CURRENT_PAGE, page)
        context.startActivity(intent)
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