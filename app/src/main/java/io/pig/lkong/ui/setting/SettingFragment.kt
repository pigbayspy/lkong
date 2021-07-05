package io.pig.lkong.ui.setting

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import io.pig.lkong.R

class SettingFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
    }
}