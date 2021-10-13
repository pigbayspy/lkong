package io.pig.lkong.ui.activity

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import io.pig.lkong.R
import io.pig.lkong.account.commom.AccountAuthenticatorActivity
import io.pig.lkong.account.const.AccountConst.ACCOUNT_TYPE
import io.pig.lkong.account.const.AccountConst.AT_TYPE_FULL_ACCESS
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_TYPE
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_AUTH
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_AVATAR
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_ID
import io.pig.lkong.account.const.AccountConst.KEY_ACCOUNT_USER_NAME
import io.pig.lkong.account.const.AccountConst.KEY_ERROR_MESSAGE
import io.pig.lkong.account.const.AccountConst.KEY_IS_ADDING_NEW_ACCOUNT
import io.pig.lkong.account.const.AccountConst.PARAM_USER_PASS
import io.pig.lkong.databinding.ActivitySignInBinding
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.snack.em.SnackTypeEnum
import io.pig.lkong.ui.snack.util.SnackBarUtil
import io.pig.lkong.util.UiUtil
import io.pig.ui.snakebar.ToastErrorConst
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import rx.android.schedulers.AndroidSchedulers

/**
 * @author yinhang
 * @since 2021/05/23
 */
class SignInActivity : AccountAuthenticatorActivity() {

    companion object {
        private const val TAG = "SignInActivity"

        private const val START_MAIN_ACTIVITY = "start_main_activity"

        private const val REQ_SIGNUP = 1
    }

    private lateinit var binding: ActivitySignInBinding
    private lateinit var accountMgr: AccountManager
    private lateinit var authTokenType: String

    private var progress: ProgressBar? = null

    private var startMainActivity = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        )

        // 初始化参数
        val cardViewLayoutParams: LayoutParams = if (isTablet()) {
            val pixelSize = resources.getDimensionPixelSize(R.dimen.width_sign_in_card_view)
            LayoutParams(pixelSize, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            val param = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val marginValue = UiUtil.dp2px(this, 16f)
            param.setMargins(marginValue, marginValue, marginValue, marginValue)
            param
        }
        cardViewLayoutParams.gravity = Gravity.CENTER
        // 设置参数
        val signInCard = findViewById<CardView>(R.id.activity_sign_in_card)
        signInCard.layoutParams = cardViewLayoutParams
        window.statusBarColor = resources.getColor(R.color.activity_bg_sign_in)
        // 获取 intent
        if (intent.hasExtra(START_MAIN_ACTIVITY)) {
            startMainActivity = intent.getBooleanExtra(START_MAIN_ACTIVITY, startMainActivity)
        }
        authTokenType = intent.getStringExtra(KEY_ACCOUNT_TYPE) ?: AT_TYPE_FULL_ACCESS
        // 获取 AccountManager
        accountMgr = AccountManager.get(baseContext)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data!!)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    fun onFaqClick(view: View) {
        AppNavigation.navigateToFaq(this)
    }

    fun onSignInClick(view: View) {
        val (result, email, password) = checkSignParam()
        if (result) {
            setLoading(true)
            Observable.create<Intent> {
                Log.d(TAG, "started authenticating")
                val userName = email.toString()
                val userPassword = password.toString()

                // 构建请求
                lifecycleScope.launch {
                    val signResult = LkongRepository.signIn(userName, userPassword)
                    val accountType = intent.getStringExtra(KEY_ACCOUNT_TYPE)

                    // 构建 Bundle
                    val data = bundleOf(
                        AccountManager.KEY_ACCOUNT_NAME to userName,
                        AccountManager.KEY_ACCOUNT_TYPE to accountType,
                        PARAM_USER_PASS to userPassword,
                        KEY_ACCOUNT_USER_ID to signResult.uid.toString(),
                        KEY_ACCOUNT_USER_NAME to signResult.name,
                        KEY_ACCOUNT_USER_AVATAR to signResult.avatar,
                        KEY_ACCOUNT_USER_AUTH to signResult.authCookie
                    )
                    // 构建 intent
                    val intent = Intent()
                    intent.putExtras(data)
                    it.onNext(intent)
                    it.onComplete()
                }
            }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Intent> {
                    override fun onSubscribe(d: Disposable?) {
                        Log.d(TAG, "begin subscribe sign in result")
                    }

                    override fun onNext(t: Intent) {
                        val errorMessage = t.getStringExtra(KEY_ERROR_MESSAGE)
                        if (errorMessage != null) {
                            showSnackBar(errorMessage, SnackTypeEnum.ERROR, Snackbar.LENGTH_SHORT)
                        } else {
                            finishLogin(t)
                        }
                    }

                    override fun onComplete() {
                        Log.d(TAG, "end subscribe sign in result")
                        setLoading(false)
                    }

                    override fun onError(e: Throwable) {
                        showSnackBar(
                            ToastErrorConst.TOAST_FAILURE_SIGN,
                            SnackTypeEnum.ERROR,
                            Snackbar.LENGTH_SHORT
                        )
                        setLoading(false)
                    }
                })
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            dismissProgressDialog()
            progress = ProgressBar(this)
            // Todo 显示
        } else {
            dismissProgressDialog()
        }
    }

    private fun dismissProgressDialog() {
        val p = progress
        if (p != null && p.visibility != View.GONE) {
            p.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    /**
     * 检查账户和密码
     */
    private fun checkSignParam(): Triple<Boolean, CharSequence, CharSequence> {
        val email = binding.signInEditEmail.text
        val password = binding.signInEditPassword.text
        if (email.isNullOrEmpty()) {
            binding.editEmailText.error = getString(R.string.input_error_email)
            return Triple(false, email, password)
        } else {
            binding.editEmailText.error = ""
        }
        if (password.isNullOrEmpty()) {
            binding.editPasswordText.error = getString(R.string.input_error_password)
            return Triple(false, email, password)
        } else {
            binding.editPasswordText.error = ""
        }
        return Triple(true, email, password)
    }

    private fun finishLogin(loginIntent: Intent) {
        val accountName = loginIntent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val accountPassword = loginIntent.getStringExtra(PARAM_USER_PASS)
        val account =
            Account(accountName, loginIntent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))

        if (intent.getBooleanExtra(KEY_IS_ADDING_NEW_ACCOUNT, false)) {
            val authToken = loginIntent.getStringExtra(AccountManager.KEY_AUTHTOKEN)
            val authTokenType: String = authTokenType

            val userId = loginIntent.getStringExtra(KEY_ACCOUNT_USER_ID)
            val userName = loginIntent.getStringExtra(KEY_ACCOUNT_USER_NAME)
            val userAvatar = loginIntent.getStringExtra(KEY_ACCOUNT_USER_AVATAR)
            val userAuth = loginIntent.getStringExtra(KEY_ACCOUNT_USER_AUTH)
            val userData = bundleOf(
                KEY_ACCOUNT_USER_ID to userId,
                KEY_ACCOUNT_USER_NAME to userName,
                KEY_ACCOUNT_USER_AVATAR to userAvatar,
                KEY_ACCOUNT_USER_AUTH to userAuth
            )
            // Creating the account on the device and setting the auth token we got
            // Not setting the auth token will cause
            // another call to the server to authenticate the user
            val accounts = accountMgr.getAccountsByType(ACCOUNT_TYPE)
            var isExist = false
            for (existAccount in accounts) {
                if (existAccount.name.compareTo(account.name) == 0) {
                    accountMgr.setUserData(account, KEY_ACCOUNT_USER_ID, userId)
                    accountMgr.setUserData(account, KEY_ACCOUNT_USER_NAME, userName)
                    accountMgr.setUserData(account, KEY_ACCOUNT_USER_AVATAR, userAvatar)
                    accountMgr.setUserData(account, KEY_ACCOUNT_USER_AUTH, userAuth)
                    isExist = true
                    break
                }
            }
            if (!isExist) {
                accountMgr.addAccountExplicitly(account, accountPassword, userData)
            }
            accountMgr.setAuthToken(account, authTokenType, authToken)
        } else {
            Log.d(TAG, "finish login and set password")
            accountMgr.setPassword(account, accountPassword)
        }
        setAccountAuthenticatorResult(intent.extras)
        setResult(RESULT_OK, loginIntent)
        // 结束
        finish()
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }

    private fun showSnackBar(errorCode: Int, type: SnackTypeEnum, length: Int) {
        SnackBarUtil.makeSimple(
            getSnackBarRootView(),
            getString(ToastErrorConst.errorCodeToStringRes(errorCode)),
            type,
            length
        ).show()
    }

    private fun showSnackBar(content: CharSequence, type: SnackTypeEnum, length: Int) {
        SnackBarUtil.makeSimple(getSnackBarRootView(), content, type, length).show()
    }

    private var rootView: View? = null

    private fun getSnackBarRootView(): View {
        if (rootView == null) {
            rootView = findViewById(android.R.id.content)
        }
        return rootView!!
    }
}