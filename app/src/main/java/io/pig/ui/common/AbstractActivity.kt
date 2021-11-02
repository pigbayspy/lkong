package io.pig.ui.common

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import io.pig.lkong.preference.BoolPrefs
import io.pig.lkong.preference.PrefConst
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.rx.RxEventBus
import io.pig.lkong.rx.event.AbstractEvent
import io.pig.lkong.rx.event.ScreenOrientationSettingsChangeEvent
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.snakebar.SnakeBarType
import io.pig.ui.snakebar.SnakeBarUtil
import io.reactivex.rxjava3.disposables.Disposable
import rx.android.schedulers.AndroidSchedulers

abstract class AbstractActivity : AppCompatActivity() {

    private lateinit var themeKey: String
    private lateinit var screenRotation: StringPrefs
    private lateinit var isNightMode: BoolPrefs
    private lateinit var sub: Disposable

    private var primaryColor = 0
    private var textPrimaryColor = 0
    private var primaryDarkColor = 0
    private var accentColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.themeKey = getThemeKey()
        this.screenRotation =
            Prefs.getStringPrefs(PrefConst.SCREEN_ROTATION, PrefConst.SCREEN_ROTATION_VALUE)
        this.checkRotation()
        this.isNightMode =
            Prefs.getBoolPrefs(PrefConst.IS_NIGHT_MODE, PrefConst.IS_NIGHT_MODE_VALUE)

        this.sub = RxEventBus.toObservable().subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onEvent)

        primaryColor = ThemeUtil.primaryColor(this, themeKey)
        primaryDarkColor = ThemeUtil.primaryColorDark(this, themeKey)
        textPrimaryColor = ThemeUtil.textColorPrimary(this, themeKey)
        accentColor = ThemeUtil.accentColor(this, themeKey)
    }

    override fun onStart() {
        super.onStart()
        checkRotation()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除订阅
        if (!sub.isDisposed) {
            sub.dispose()
        }
    }

    protected fun isNightMode(): Boolean {
        return isNightMode.get()
    }

    protected open fun getPrimaryColor(): Int {
        return primaryColor
    }

    protected open fun getPrimaryDarkColor(): Int {
        return primaryDarkColor
    }

    protected open fun getAccentColor(): Int {
        return accentColor
    }

    protected fun toggleNightMode() {
        isNightMode.set(!isNightMode.get())
        ThemeUtil.markChanged(this, ThemeUtil.LIGHT_THEME, ThemeUtil.DARK_THEME)
        recreate()
    }

    fun getThemeKey(): String {
        return if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(ThemeUtil.DARK_THEME, false)
        ) {
            ThemeUtil.DARK_THEME
        } else {
            ThemeUtil.LIGHT_THEME
        }
    }

    fun showSnakeBar(rootView: View, text: CharSequence, type: SnakeBarType) {
        SnakeBarUtil.makeSimple(
            rootView,
            text,
            type,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    protected fun isActivityDestroyed(): Boolean {
        return isDestroyed || isFinishing
    }

    protected fun processToolbar(toolbar: Toolbar) {
        val key = getThemeKey()
        val toolbarColor = ThemeUtil.toolbarColor(this, key)
        val tintColor = ThemeUtil.getToolbarTitleColor(this, key, toolbarColor)
        val root = toolbar.parent
        if (root is AppBarLayout) {
            root.setBackgroundColor(toolbarColor)
            toolbar.setTitleTextColor(tintColor)
        }
        toolbar.setSubtitleTextColor(ThemeUtil.getToolbarSubtitleColor(this, key, toolbarColor))
    }

    private fun checkRotation() {
        requestedOrientation = when (screenRotation.get()) {
            "0" -> ActivityInfo.SCREEN_ORIENTATION_USER
            "1" -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            "2" -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_USER
        }
    }

    fun onEvent(event: AbstractEvent) {
        if (event is ScreenOrientationSettingsChangeEvent) {
            checkRotation()
        }
    }
}