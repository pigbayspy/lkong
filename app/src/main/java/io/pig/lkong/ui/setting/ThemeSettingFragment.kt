package io.pig.lkong.ui.setting

import android.graphics.Color.*
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import io.pig.lkong.R
import io.pig.lkong.theme.ThemeConfig
import io.pig.lkong.ui.dialog.TextSizeDialog
import io.pig.lkong.util.TextSizeUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.ColorPreference
import io.pig.ui.common.getThemeKey

class ThemeSettingFragment : PreferenceFragmentCompat() {

    private val themeKey: String by lazy {
        requireActivity().getThemeKey()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_theme_setting, rootKey)
        invalidateSettings()
    }

    private fun invalidateSettings() {
        val attachActivity = requireActivity()
        val config = ThemeConfig(requireContext(), themeKey)
        val primaryColorPref: ColorPreference = findPreference(ThemeUtil.KEY_PRIMARY_COLOR)!!
        primaryColorPref.setColor(ThemeUtil.primaryColor(attachActivity, themeKey), BLACK)
        primaryColorPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity)
                .title(R.string.setting_theme_primary_color)
                .colorChooser(
                    ColorPalette.Accent,
                    initialSelection = ThemeUtil.primaryColor(attachActivity, themeKey)
                ) { _, color ->
                    config.primaryColor(color)
                }
                .positiveButton(R.string.setting_theme_select)
                .show()
            return@setOnPreferenceClickListener true
        }
        val accentColorPref: ColorPreference = findPreference(ThemeUtil.KEY_ACCENT_COLOR)!!
        accentColorPref.setColor(ThemeUtil.accentColor(attachActivity, themeKey), BLACK)
        accentColorPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity).title(R.string.setting_theme_accent_color)
                .colorChooser(
                    ColorPalette.Accent,
                    initialSelection = ThemeUtil.accentColor(attachActivity, themeKey)
                ) { _, color ->
                    config.accentColor(color)
                }
                .show()
            return@setOnPreferenceClickListener true
        }
        val textColorPrimaryPref: ColorPreference = findPreference("text_primary")!!
        textColorPrimaryPref.setColor(ThemeUtil.textColorPrimary(attachActivity, themeKey), BLACK)
        textColorPrimaryPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity)
                .title(R.string.setting_theme_primary_text_color)
                .colorChooser(
                    ColorPalette.Accent,
                    initialSelection = ThemeUtil.textColorPrimary(attachActivity, themeKey)
                ) { _, color ->
                    config.textColorPrimary(color)
                }.show()

            return@setOnPreferenceClickListener true
        }
        val textColorSecondaryPref: ColorPreference = findPreference("text_secondary")!!
        textColorSecondaryPref.setColor(
            ThemeUtil.textColorSecondary(attachActivity, themeKey),
            BLACK
        )
        textColorSecondaryPref.setOnPreferenceClickListener {
            MaterialDialog(attachActivity).title(R.string.setting_theme_secondary_text_color)
                .colorChooser(
                    ColorPalette.Accent,
                    initialSelection = ThemeUtil.textColorSecondary(attachActivity, themeKey)
                ) { _, color ->
                    config.textColorSecondary(color)

                }.show()
            return@setOnPreferenceClickListener true
        }
        val darkThemePreference: Preference = findPreference(ThemeUtil.DARK_THEME)!!
        darkThemePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _: Preference?, _: Any? ->
                ThemeUtil.markChanged(attachActivity, ThemeUtil.LIGHT_THEME)
                ThemeUtil.markChanged(attachActivity, ThemeUtil.DARK_THEME)
                attachActivity.recreate()
                return@OnPreferenceChangeListener true
            }
        val lightStatusMode: Preference =
            findPreference(ThemeUtil.KEY_LIGHT_STATUS_BAR_MODE)!!
        val lightToolbarMode: Preference =
            findPreference(ThemeUtil.KEY_LIGHT_TOOLBAR_MODE)!!
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