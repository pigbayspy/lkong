package io.pig.lkong.account

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.text.TextUtils
import android.util.Log
import io.pig.lkong.MainActivity
import io.pig.lkong.account.const.AccountConst.ACCOUNT_TYPE
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_AUTH
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_AVATAR
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_ID
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_NAME
import io.pig.lkong.exception.SignInException
import io.pig.lkong.preference.LongPrefs
import io.pig.lkong.preference.PrefConst.DEFAULT_ACCOUNT_UID
import io.pig.lkong.preference.PrefConst.DEFAULT_ACCOUNT_UID_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.rx.RxEventBus
import io.pig.lkong.rx.event.AccountChangeEvent
import io.pig.lkong.rx.event.AccountCreateEvent
import io.pig.lkong.rx.event.AccountRemoveEvent
import java.util.*
import javax.inject.Inject

/**
 * 用户账户管理器
 *
 * @author yinhang
 * @since 2021/5/16
 */
class UserAccountManager {

    companion object {
        const val TAG = "UserAccountManager"

        private const val THREAD_NAME = "UserAccountManagerThread"

        private val handler: Handler

        init {
            val thread = HandlerThread(THREAD_NAME)
            thread.start()
            handler = Handler(thread.looper)
        }

        fun getUserAccountFromAccountManager(
            account: Account,
            accountManager: AccountManager
        ): UserAccount {
            val idString = accountManager.getUserData(account, KEY_ACCOUNT_USER_ID)
            val userId = java.lang.Long.valueOf(if (TextUtils.isEmpty(idString)) "0" else idString)
            val userEmail = account.name
            val userName = accountManager.getUserData(account, KEY_ACCOUNT_USER_NAME)
            val userAvatar =
                accountManager.getUserData(account, KEY_ACCOUNT_USER_AVATAR)
            val userAuth = accountManager.getUserData(account, KEY_ACCOUNT_USER_AUTH)
            return UserAccount(
                account,
                userId,
                userName,
                userEmail,
                userAvatar,
                userAuth,
            )
        }
    }

    private val userAccounts = mutableMapOf<Long, UserAccount>()

    private val defaultAccountUid: LongPrefs =
        Prefs.getLongPrefs(DEFAULT_ACCOUNT_UID, DEFAULT_ACCOUNT_UID_VALUE)

    @Inject
    lateinit var accountMgr: AccountManager

    @Inject
    lateinit var context: Context

    private var currentAccount: UserAccount? = null
    private var authObject: LkongAuthObject? = null

    /**
     * 用户是否登录
     */
    fun isSignedIn(): Boolean {
        return userAccounts.isNotEmpty()
    }

    /**
     * 返回所有用户账户
     */
    fun getUserAccounts(): List<UserAccount> {
        val userAccounts: List<UserAccount> = ArrayList(userAccounts.values)
        val currentUserAccount = currentAccount ?: return userAccounts
        val currentUserId = currentUserAccount.userId
        for (i in userAccounts.indices) {
            if (userAccounts[i].userId == currentUserId) {
                Collections.swap(userAccounts, 0, i)
                break
            }
        }
        return userAccounts
    }

    fun init() {
        update()
        accountMgr.addOnAccountsUpdatedListener(
            { accountArr ->
                synchronized(this) {
                    if (accountArr == null) {
                        return@addOnAccountsUpdatedListener
                    }
                    val lkongAccounts = mutableListOf<Account>()
                    for (account in accountArr) {
                        if (account.type.equals(ACCOUNT_TYPE, true)) {
                            lkongAccounts.add(account)
                        }
                    }
                    if (lkongAccounts.size != userAccounts.size) {
                        Log.w(TAG, "Account count change!")
                    }
                    if (userAccounts.isEmpty() && lkongAccounts.isNotEmpty()) {
                        update()
                        if (!MainActivity.Running.get()) {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }
                        RxEventBus.sendEvent(AccountChangeEvent())
                    } else if (lkongAccounts.size > userAccounts.size) {
                        update()
                        RxEventBus.sendEvent(AccountCreateEvent())
                    } else if (lkongAccounts.size < userAccounts.size && userAccounts.isNotEmpty()) {
                        update()
                        RxEventBus.sendEvent(AccountRemoveEvent())
                    } else {
                        update()
                    }
                }
            }, handler, true
        )
        Log.d(TAG, "UserAccountManager init done")
    }

    fun removeAllAccounts(): Boolean {
        val accountArr = accountMgr.getAccountsByType(ACCOUNT_TYPE)
        var successCount = 0
        for (account in accountArr) {
            if (accountMgr.removeAccountExplicitly(account)) {
                ++successCount
            }
        }
        return successCount == accountArr.size
    }

    /**
     * 返回当前用户账户
     *
     * @throws SignInException 未登录异常
     */
    fun getCurrentUserAccount(): UserAccount {
        if (currentAccount != null) {
            return currentAccount!!
        }
        if (currentAccount == null && userAccounts.isNotEmpty()) {
            var current = userAccounts[defaultAccountUid.get()]
            if (current == null) {
                current = getFirstAccount()
            }
            if (current != null) {
                currentAccount = current
                defaultAccountUid.set(current.userId)
                authObject = getAuthObject(current)
                return current
            }
        }
        throw SignInException("need to sign in")
    }

    private fun setCurrentUserAccount(userId: Long) {
        this.currentAccount = userAccounts[userId]
        this.authObject = getAuthObject(currentAccount!!)
        this.defaultAccountUid.set(userId)
    }

    /**
     * 更新数据
     */
    @Synchronized
    private fun update() {
        userAccounts.clear()
        val accountArr = accountMgr.getAccountsByType(ACCOUNT_TYPE)
        for (account in accountArr) {
            val userAccount = getUserFromAccount(account)
            if (userAccount != null) {
                userAccounts[userAccount.userId] = userAccount
            }
        }
        val uid = defaultAccountUid.get()
        val defaultAccount = userAccounts[uid]
        val firstAccount = getFirstAccount()
        if (defaultAccount == null && firstAccount != null) {
            setCurrentUserAccount(firstAccount.userId)
        } else if (currentAccount != null) {
            setCurrentUserAccount(currentAccount!!.userId)
        } else {
            defaultAccountUid.set(DEFAULT_ACCOUNT_UID_VALUE)
        }
    }

    fun getAuthObject(): LkongAuthObject {
        if (authObject == null) {
            val account = currentAccount
            if (account == null) {
                setCurrentUserAccount(getFirstAccount()!!.userId)
            }
            authObject = getAuthObject(currentAccount!!)
        }
        return authObject!!
    }

    private fun getAuthObject(account: UserAccount): LkongAuthObject {
        return LkongAuthObject(
            account.userId,
            account.userName,
            account.authCookie
        )
    }

    private fun getFirstAccount(): UserAccount? {
        if (userAccounts.isEmpty()) {
            return null
        }
        return userAccounts.iterator().next().value
    }

    /**
     * Account 转换为 UserAccount
     */
    private fun getUserFromAccount(account: Account): UserAccount? {
        val idStr = accountMgr.getUserData(account, KEY_ACCOUNT_USER_ID)
        val userName = accountMgr.getUserData(account, KEY_ACCOUNT_USER_NAME)
        val userAvatar = accountMgr.getUserData(account, KEY_ACCOUNT_USER_AVATAR)
        val userAuth = accountMgr.getUserData(account, KEY_ACCOUNT_USER_AUTH)
        val id = idStr.toLong()
        val userEmail = account.name
        if (userAuth.isNullOrEmpty()) {
            return null
        }
        return UserAccount(
            account,
            id,
            userName,
            userEmail,
            userAvatar,
            userAuth
        )
    }
}