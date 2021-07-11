package io.pig.lkong.ui.setting

import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import io.pig.lkong.R
import io.pig.lkong.theme.ThemeConfig
import io.pig.lkong.ui.dialog.TextSizeDialog
import io.pig.lkong.util.TextSizeUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.ColorPreference
import io.pig.ui.common.getThemeKey

class ThemeSettingFragment : PreferenceFragmentCompat() {

    private lateinit var themeKey: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_theme_setting, rootKey)
        invalidateSettings()
    }

    private fun invalidateSettings() {
        val attachActivity = requireActivity()
        val colors = intArrayOf(RED, GREEN, BLUE)
        themeKey = attachActivity.getThemeKey()
        val primaryColorPref: Preference = findPreference("primary_color")!!
        // primaryColorPref.setColor(ThemeUtil.primaryColor(attachActivity, themeKey), Color.BLACK)
        primaryColorPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity).show {
                title(R.string.setting_theme_primary_color)
                colorChooser(
                    colors,
                    initialSelection = ThemeUtil.primaryColor(attachActivity, themeKey)
                )
            }
            return@setOnPreferenceClickListener true
        }
        val accentColorPref: ColorPreference = findPreference("accent_color")!!
        accentColorPref.setColor(ThemeUtil.accentColor(attachActivity, themeKey), Color.BLACK)
        accentColorPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity).show {
                title(R.string.setting_theme_accent_color)
                colorChooser(
                    colors,
                    initialSelection = ThemeUtil.accentColor(attachActivity, themeKey)
                )
            }
            return@setOnPreferenceClickListener true
        }
        val textColorPrimaryPref: Preference = findPreference("text_primary")!!
        // textColorPrimaryPref.setColor(ThemeUtil.textColorPrimary(attachActivity, themeKey), BLACK)
        textColorPrimaryPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity).show {
                title(R.string.setting_theme_primary_text_color)
                colorChooser(
                    colors,
                    initialSelection = ThemeUtil.textColorPrimary(attachActivity, themeKey)
                )
            }
            return@setOnPreferenceClickListener true
        }
        val textColorSecondaryPref: Preference = findPreference("text_secondary")!!
        // textColorSecondaryPref.setColor(ThemeUtil.textColorSecondary(attachActivity, themeKey), BLACK)
        textColorSecondaryPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity).show {
                title(R.string.setting_theme_secondary_text_color)
                colorChooser(
                    colors,
                    initialSelection = ThemeUtil.textColorSecondary(attachActivity, themeKey)
                )
            }
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
        lightStatusMode.setOnPreferenceChangeListener { _, newValue ->
            val constant: Int = (newValue as String).toInt()
            ThemeConfig(attachActivity, themeKey)
                .lightStatusBarMode(constant)
                .apply(attachActivity)
            return@setOnPreferenceChangeListener true
        }
        lightToolbarMode.setOnPreferenceChangeListener { _, newValue ->
            val constant: Int = (newValue as String).toInt()
            ThemeConfig(attachActivity, themeKey)
                .lightToolbarMode(constant)
                .apply(attachActivity)
            return@setOnPreferenceChangeListener true
        }
        val statusBarPref: SwitchPreference =
            findPreference("colored_status_bar")!!
        val navBarPref: SwitchPreference =
            findPreference("colored_nav_bar")!!
        statusBarPref.isChecked = ThemeUtil.coloredStatusBar(attachActivity, themeKey)
        statusBarPref.setOnPreferenceChangeListener { _, newValue ->
            ThemeConfig(attachActivity, themeKey)
                .coloredStatusBar(newValue as Boolean)
                .apply(attachActivity)
            return@setOnPreferenceChangeListener true
        }
        navBarPref.isChecked = ThemeUtil.coloredNavigationBar(attachActivity, themeKey)
        navBarPref.setOnPreferenceChangeListener { _, newValue ->
            ThemeConfig(attachActivity, themeKey)
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
                    preference.title,
                    true
                )
                return@OnPreferenceClickListener false
            }
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