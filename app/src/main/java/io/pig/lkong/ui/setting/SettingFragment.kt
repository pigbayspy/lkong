package io.pig.lkong.ui.setting

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.pig.lkong.R
import io.pig.lkong.preference.PrefConst

class SettingFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
        setImagePolicySummary()
    }

    private fun setImagePolicySummary() {
        val downloadPolicyString = preferenceManager.sharedPreferences.getString(
            PrefConst.IMAGE_DOWNLOAD_POLICY,
            PrefConst.IMAGE_DOWNLOAD_POLICY_VALUE
        )
        val imageDownloadPolicy =
            downloadPolicyString?.toInt() ?: PrefConst.IMAGE_DOWNLOAD_POLICY_VALUE.toInt()
        val policyArray =
            requireActivity().resources.getStringArray(R.array.setting_image_download_policy_values_array)
        val imageDownloadPolicyPrefs: Preference =
            findPreference(PrefConst.IMAGE_DOWNLOAD_POLICY)!!
        imageDownloadPolicyPrefs.summary = policyArray[imageDownloadPolicy]
    }
}