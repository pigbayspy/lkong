package io.pig.ui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.pig.lkong.rx.RxEventBus
import io.pig.lkong.rx.event.AbstractEvent
import io.pig.lkong.util.ThemeUtil
import io.reactivex.rxjava3.disposables.Disposable
import rx.android.schedulers.AndroidSchedulers

abstract class AbstractFragment : Fragment() {

    private lateinit var themeKey: String

    private lateinit var sub: Disposable

    private var primaryColor = 0
    private var textPrimaryColor = 0
    private var primaryDarkColor = 0
    private var accentColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.themeKey = getThemeKey()
        this.sub = RxEventBus.toObservable().subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onEvent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        primaryColor = ThemeUtil.primaryColor(activity, themeKey)
        primaryDarkColor = ThemeUtil.primaryColorDark(activity, themeKey)
        accentColor = ThemeUtil.accentColor(activity, themeKey)
        activity.invalidateOptionsMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除订阅
        if (!sub.isDisposed) {
            sub.dispose()
        }
    }

    protected fun getThemeKey(): String {
        return if (PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getBoolean(ThemeUtil.DARK_THEME, false)
        ) {
            ThemeUtil.DARK_THEME
        } else {
            ThemeUtil.LIGHT_THEME
        }
    }

    fun onEvent(event: AbstractEvent) {
        // Nothing to do
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
}