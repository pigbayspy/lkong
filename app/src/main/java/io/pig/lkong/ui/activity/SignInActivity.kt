package io.pig.lkong.ui.activity

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import io.pig.lkong.R
import io.pig.lkong.account.const.AccountConst
import io.pig.lkong.databinding.ActivitySignInBinding
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.util.UiUtil
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers

class SignInActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SignInActivity"

        const val START_MAIN_ACTIVITY = "start_main_activity"
        const val ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE"
        const val ARG_AUTH_TYPE = "AUTH_TYPE"
        const val ARG_ACCOUNT_NAME = "ACCOUNT_NAME"

        private const val KEY_ERROR_MESSAGE = "ERR_MSG"
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
        // 获取 intent
        if (intent.hasExtra(START_MAIN_ACTIVITY)) {
            startMainActivity = intent.getBooleanExtra(START_MAIN_ACTIVITY, startMainActivity)
        }
        authTokenType = intent.getStringExtra(ARG_ACCOUNT_TYPE) ?: AccountConst.AT_TYPE_FULL_ACCESS
        // 获取 AccountManager
        accountMgr = AccountManager.get(baseContext)
    }

    fun onFaqClick(view: View) {
        AppNavigation.navigateToFaq(this)
    }

    fun onSignInClick(view: View) {
        val (result, email, password) = checkSignParam()
        if (result) {
            setLoading(true)
            Observable.create<Intent> {
                Log.e(TAG, "started authenticating")
                val userName = email.toString()
                val userPassword = password.toString()
                val accountType = intent.getStringExtra(ARG_ACCOUNT_TYPE)
                val data = Bundle()
                // Todo 调用登录接口
                val intent = Intent()
                intent.putExtras(data)
                it.onNext(intent)
                it.onComplete()
            }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Intent> {
                    override fun onSubscribe(d: Disposable?) {
                        Log.d(TAG, "begin subscribe sign in result")
                    }

                    override fun onNext(t: Intent) {
                        if (t.hasExtra(KEY_ERROR_MESSAGE)) {
                            // Todo
                        } else {
                            // Todo
                        }
                    }

                    override fun onComplete() {
                        Log.d(TAG, "end subscribe sign in result")
                        setLoading(false)
                    }

                    override fun onError(e: Throwable) {
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

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}