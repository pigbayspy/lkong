package io.pig.lkong.ui.setting

import android.graphics.Color
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.pig.lkong.R
import io.pig.lkong.ui.dialog.TextSizeDialog
import io.pig.lkong.util.TextSizeUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.getThemeKey

class ThemeSettingFragment : PreferenceFragmentCompat() {

    private lateinit var themeKey: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_theme_setting, rootKey)
        invalidateSettings()
    }

    private fun invalidateSettings() {
        val attachActivity = requireActivity()
        themeKey = attachActivity.getThemeKey()
        val primaryColorPref: Preference = findPreference("primary_color")!!
        primaryColorPref.setColor(ThemeUtil.primaryColor(attachActivity, themeKey), Color.BLACK)
        primaryColorPref.setOnPreferenceClickListener {
            Builder(attachActivity, R.string.setting_theme_primary_color)
                .preselect(ThemeUtil.primaryColor(attachActivity, themeKey))
                .show()
            true
        }
        val accentColorPref: Preference = findPreference("accent_color")!!
        accentColorPref.setColor(ThemeUtil.accentColor(attachActivity, themeKey), Color.BLACK)
        accentColorPref.setOnPreferenceClickListener {
            Builder(attachActivity, R.string.setting_theme_accent_color)
                .preselect(ThemeUtil.accentColor(attachActivity, themeKey))
                .show()
            true
        }
        val textColorPrimaryPref: Preference = findPreference("text_primary")!!
        textColorPrimaryPref.setColor(
            ThemeUtil.textColorPrimary(attachActivity, themeKey),
            Color.BLACK
        )
        textColorPrimaryPref.setOnPreferenceClickListener {
            Builder(
                attachActivity,
                R.string.setting_theme_primary_text_color
            )
                .preselect(ThemeUtil.textColorPrimary(attachActivity, themeKey))
                .show()
            return@setOnPreferenceClickListener true
        }
        val textColorSecondaryPref: Preference = findPreference("text_secondary")!!
        textColorSecondaryPref.setColor(
            ThemeUtil.textColorSecondary(attachActivity, themeKey),
            Color.BLACK
        )
        textColorSecondaryPref.setOnPreferenceClickListener {
            Builder(
                attachActivity,
                R.string.setting_theme_secondary_text_color
            )
                .preselect(ThemeUtil.textColorSecondary(attachActivity, themeKey))
                .show()
            return@setOnPreferenceClickListener true
        }
        val darkThemePreference: Preference = findPreference("dark_theme")!!
        darkThemePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _: Preference?, _: Any? ->
                ThemeUtil.markChanged(attachActivity, "light_theme")
                ThemeUtil.markChanged(attachActivity, "dark_theme")
                attachActivity.recreate()
                return@OnPreferenceChangeListener true
            }
        val lightStatusMode: Preference =
            findPreference("light_status_bar_mode")!!
        val lightToolbarMode: Preference =
            findPreference("light_toolbar_mode")!!
        lightStatusMode.isEnabled = true
        lightStatusMode.setOnPreferenceChangeListener { _, _ ->
            @Config.LightStatusBarMode val constant: Int = newValue as String?. toInt ()
            ATE.config(attachActivity, themeKey)
                .lightStatusBarMode(constant)
                .apply(attachActivity)
            true
        }
        lightToolbarMode.setOnPreferenceChangeListener { _, _ ->
            @Config.LightToolbarMode val constant: Int = newValue as String?. toInt ()
            ATE.config(activity, themeKey)
                .lightToolbarMode(constant)
                .apply(activity)
            true
        }
        val statusBarPref: Preference =
            findPreference("colored_status_bar")!!
        val navBarPref: Preference =
            findPreference("colored_nav_bar")!!
        statusBarPref.setChecked(ThemeUtil.coloredStatusBar(attachActivity, themeKey))
        statusBarPref.setOnPreferenceChangeListener { _, _ ->
            ATE.config(attachActivity, themeKey)
                .coloredStatusBar(newValue as Boolean)
                .apply(attachActivity)
            return@setOnPreferenceChangeListener true
        }
        navBarPref.setChecked(ThemeUtil.coloredNavigationBar(attachActivity, themeKey))
        navBarPref.setOnPreferenceChangeListener { _, _ ->
            ATE.config(attachActivity, themeKey)
                .coloredNavigationBar(newValue as Boolean)
                .apply(attachActivity)
            return@setOnPreferenceChangeListener true
        }
        val textSizeClickListener =
            Preference.OnPreferenceClickListener { preference ->
                TextSizeDialog.show(
                    attachActivity,
                    preference.key,
                    themeKey,
                    preference.titleRes,
                    true
                )
                return@OnPreferenceClickListener false
            }
        val textSizeHeadline: Preference = findPreference("text_size|headline")!!

        textSizeHeadline.onPreferenceClickListener = textSizeClickListener
        textSizeHeadline.summary = getString(
            R.string.setting_theme_headline_text_size_desc,
            TextSizeUtil.pxToSp(
                this,
                TextSizeUtil.textSizeForMode(
                    attachActivity,
                    themeKey,
                    TextSizeUtil.TEXT_SIZE_HEADLINE
                )
            )
        )

        val textSizeTitle: Preference = findPreference("text_size|title")!!

        textSizeTitle.onPreferenceClickListener = textSizeClickListener
        textSizeTitle.summary = getString(
            R.string.setting_theme_title_text_size_desc,
            TextSizeUtil.pxToSp(
                this,
                TextSizeUtil.textSizeForMode(attachActivity, themeKey, TextSizeUtil.TEXT_SIZE_TITLE)
            )
        )

        val textSizeSubheading: Preference = findPreference("text_size|subheading")!!
        textSizeSubheading.onPreferenceClickListener = textSizeClickListener
        textSizeSubheading.summary = getString(
            R.string.setting_theme_subheading_text_size_desc,
            TextSizeUtil.pxToSp(
                this,
                TextSizeUtil.textSizeForMode(
                    attachActivity,
                    themeKey,
                    TextSizeUtil.TEXT_SIZE_SUBHEADING
                )
            )
        )

        val textSizeBody: Preference = findPreference("text_size|body")!!
        textSizeBody.onPreferenceClickListener = textSizeClickListener
        textSizeBody.summary = getString(
            R.string.setting_theme_body_text_size_desc,
            TextSizeUtil.pxToSp(
                this,
                TextSizeUtil.textSizeForMode(attachActivity, themeKey, TextSizeUtil.TEXT_SIZE_BODY)
            )
        )

    }
}