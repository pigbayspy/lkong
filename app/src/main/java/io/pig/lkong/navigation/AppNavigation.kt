package io.pig.lkong.navigation

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import io.pig.lkong.R
import io.pig.lkong.account.const.AccountConst
import io.pig.lkong.application.const.AppConst
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.model.ForumModel
import io.pig.lkong.sync.SyncUtil
import io.pig.lkong.ui.forum.content.ForumThreadActivity
import io.pig.lkong.ui.notify.NotifyActivity
import io.pig.lkong.ui.pm.PmActivity
import io.pig.lkong.ui.post.list.PostListActivity
import io.pig.lkong.ui.profile.UserProfileActivity

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

    fun navigateToManageAccount(context: Context) {
        val intent = Intent(Settings.ACTION_SYNC_SETTINGS)
        intent.putExtra(
            Settings.EXTRA_AUTHORITIES,
            arrayOf(SyncUtil.SYNC_AUTHORITY_CHECK_NOTICE)
        )
        context.startActivity(intent)
    }

    fun navigateToFaq(context: Context) {
        openUrl(AppConst.FAQ_URL, context)
    }

    fun openUserProfileActivity(context: Context, uid: Long) {
        val intent = Intent(context, UserProfileActivity::class.java)
        intent.putExtra(DataContract.BUNDLE_USER_ID, uid)
        context.startActivity(intent)
    }

    fun openForumContentActivity(
        context: Context,
        forum: ForumModel
    ) {
        val intent = Intent(context, ForumThreadActivity::class.java)
        intent.putExtra(DataContract.BUNDLE_FORUM_ID, forum.fid)
        intent.putExtra(DataContract.BUNDLE_FORUM_NAME, forum.name)
        intent.putExtra(DataContract.BUNDLE_FORUM_AVATAR, forum.avatar)
        context.startActivity(intent)
    }

    fun openPostListActivity(context: Context, threadId: Long, page: Int = 1) {
        val intent = Intent(context, PostListActivity::class.java)
        intent.putExtra(DataContract.BUNDLE_THREAD_ID, threadId)
        intent.putExtra(DataContract.BUNDLE_THREAD_CURRENT_PAGE, page)
        context.startActivity(intent)
    }

    fun openPmActivity(
        context: Context,
        userId: Long,
        username:String,
        userAvatar:String?
    ) {
        val intent = Intent(context, PmActivity::class.java)
        intent.putExtra(DataContract.BUNDLE_USER_ID, userId)
        intent.putExtra(DataContract.BUNDLE_USER_NAME, username)
        intent.putExtra(DataContract.BUNDLE_USER_AVATAR, userAvatar)
        context.startActivity(intent)
    }

    fun openNewThreadActivity(context: Context, forumId: Long, forumName: String) {
        val intent = Intent(context, null)
        intent.putExtra(DataContract.BUNDLE_FORUM_ID, forumId)
        intent.putExtra(DataContract.BUNDLE_FORUM_NAME, forumName)
        context.startActivity(intent)
    }

    fun openNotifyActivity(context: Context) {
        val intent = Intent(context, NotifyActivity::class.java)
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