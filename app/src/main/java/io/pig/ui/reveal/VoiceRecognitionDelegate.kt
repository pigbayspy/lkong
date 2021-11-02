package io.pig.ui.reveal

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

abstract class VoiceRecognitionDelegate {
    private val mVoiceRecognitionRequestCode: Int
    private var activity: Activity? = null
    private var fragment: Fragment? = null

    constructor(activity: Activity, activityRequestCode: Int = DEFAULT_VOICE_REQUEST_CODE) {
        this.activity = activity
        mVoiceRecognitionRequestCode = activityRequestCode
    }

    constructor(fragment: Fragment, activityRequestCode: Int = DEFAULT_VOICE_REQUEST_CODE) {
        this.fragment = fragment
        mVoiceRecognitionRequestCode = activityRequestCode
    }

    fun onStartVoiceRecognition() {
        if (activity != null) {
            val intent = buildVoiceRecognitionIntent()
            activity!!.startActivityForResult(intent, mVoiceRecognitionRequestCode)
        } else if (fragment != null) {
            val intent = buildVoiceRecognitionIntent()
            fragment!!.startActivityForResult(intent, mVoiceRecognitionRequestCode)
        }
    }

    fun getContext(): Context {
        activity?.let {
            return it
        }
        fragment?.let {
            return it.requireContext()
        }
        throw IllegalStateException("Could not get context in VoiceRecognitionDelegate.")
    }

    abstract fun buildVoiceRecognitionIntent(): Intent
    abstract val isVoiceRecognitionAvailable: Boolean

    companion object {
        const val DEFAULT_VOICE_REQUEST_CODE = 8185102
    }
}