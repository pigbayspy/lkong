package io.pig.lkong.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.pig.lkong.R
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.PrefConst
import io.pig.lkong.rx.RxEventBus
import io.pig.lkong.rx.event.ScreenOrientationSettingsChangeEvent
import io.pig.lkong.sync.SyncUtil
import javax.inject.Inject

class SettingFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingViewModel

    @Inject
    lateinit var userAccountManager: UserAccountManager

    private val onConcisePreferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                PrefConst.IMAGE_DOWNLOAD_POLICY -> setImagePolicySummary()
                PrefConst.AVATAR_DOWNLOAD_POLICY -> setAvatarPolicySummary()
                PrefConst.SCREEN_ROTATION -> RxEventBus.sendEvent(
                    ScreenOrientationSettingsChangeEvent()
                )
                PrefConst.CHECK_NOTIFICATION_DURATION -> {
                    val newDurationString = sharedPreferences.getString(
                        key,
                        PrefConst.CHECK_NOTIFICATION_DURATION_VALUE
                    ) ?: PrefConst.CHECK_NOTIFICATION_DURATION_VALUE
                    val newDuration = newDurationString.toLong()
                    SyncUtil.setPeriodicSync(
                        userAccountManager.getCurrentUserAccount().account,
                        SyncUtil.SYNC_AUTHORITY_CHECK_NOTICE,
                        false,
                        newDuration
                    )
                }
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
        injectThis()
        setImagePolicySummary()
        setAvatarPolicySummary()

        // 账户设置
        val syncPrefs: Preference = findPreference("prefs_goto_account_settings")!!
        syncPrefs.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            AppNavigation.navigateToManageAccount(requireActivity())
            return@OnPreferenceClickListener true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(
            onConcisePreferenceChangedListener
        )
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(
            onConcisePreferenceChangedListener
        )
    }

    private fun injectThis() {
        LkongApplication.get(requireContext()).presentComponent().inject(this)
    }

    private fun setImagePolicySummary() {
        val downloadPolicyString = preferenceManager.sharedPreferences.getString(
            PrefConst.IMAGE_DOWNLOAD_POLICY,
            PrefConst.IMAGE_DOWNLOAD_POLICY_VALUE
        ) ?: PrefConst.IMAGE_DOWNLOAD_POLICY_VALUE
        val imageDownloadPolicy =
            downloadPolicyString.toInt()
        val policyArray =
            requireActivity().resources.getStringArray(R.array.setting_image_download_policy_values_array)
        val imageDownloadPolicyPrefs: Preference =
            findPreference(PrefConst.IMAGE_DOWNLOAD_POLICY)!!
        imageDownloadPolicyPrefs.summary = policyArray[imageDownloadPolicy]
    }

    private fun setAvatarPolicySummary() {
        val avatarPolicyString = preferenceManager.sharedPreferences.getString(
            PrefConst.AVATAR_DOWNLOAD_POLICY,
            PrefConst.AVATAR_DOWNLOAD_POLICY_VALUE
        ) ?: PrefConst.AVATAR_DOWNLOAD_POLICY_VALUE
        val imageDownloadPolicy =
            avatarPolicyString.toInt()
        val policyArray =
            requireActivity().resources.getStringArray(R.array.setting_image_download_policy_values_array)
        val avatarDownloadPolicyPrefs: Preference =
            findPreference(PrefConst.AVATAR_DOWNLOAD_POLICY)!!
        avatarDownloadPolicyPrefs.summary = policyArray[imageDownloadPolicy]
    }
}