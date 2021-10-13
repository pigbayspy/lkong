package io.pig.lkong.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import io.pig.lkong.account.const.AccountConst.AUTH_LABEL
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_NAME
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_TYPE
import io.pig.lkong.account.const.AccountConst.KEY_AUTH_TYPE
import io.pig.lkong.account.const.AccountConst.KEY_IS_ADDING_NEW_ACCOUNT
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.ui.activity.SignInActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/5/17
 */
class LkongAuthenticator(private val context: Context) :
    AbstractAccountAuthenticator(context) {

    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle {
        val accountMgr = AccountManager.get(context)
        var authToken = accountMgr.peekAuthToken(account, authTokenType)
        if (authToken.isNullOrEmpty()) {
            val password = accountMgr.getPassword(account)
            if (password.isNotEmpty()) {
                GlobalScope.launch {
                    val result = LkongRepository.signIn(account.name, password)
                    if (result.authCookie.isNotEmpty()) {
                        authToken = result.authCookie
                    }
                }
            }
        }
        if (authToken.isNotEmpty()) {
            return bundleOf(
                AccountManager.KEY_ACCOUNT_NAME to account.name,
                AccountManager.KEY_ACCOUNT_TYPE to account.type,
                AccountManager.KEY_AUTHTOKEN to authToken
            )
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        val intent = Intent(context, SignInActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(KEY_ACCOUNT_TYPE, account.type)
        intent.putExtra(KEY_AUTH_TYPE, authTokenType)
        intent.putExtra(KEY_ACCOUNT_NAME, account.name)
        return bundleOf(AccountManager.KEY_INTENT to intent)
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        return AUTH_LABEL
    }

    override fun editProperties(
        response: AccountAuthenticatorResponse?,
        accountType: String?
    ): Bundle? {
        return null
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        val intent = Intent(context, SignInActivity::class.java)
        intent.putExtra(KEY_ACCOUNT_TYPE, accountType)
        intent.putExtra(KEY_AUTH_TYPE, authTokenType)
        intent.putExtra(KEY_IS_ADDING_NEW_ACCOUNT, true)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        return bundleOf(AccountManager.KEY_INTENT to intent)
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account,
        options: Bundle?
    ): Bundle? {
        return null
    }

    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account,
        features: Array<out String>
    ): Bundle {
        return bundleOf(AccountManager.KEY_BOOLEAN_RESULT to false)
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? {
        return null
    }
}