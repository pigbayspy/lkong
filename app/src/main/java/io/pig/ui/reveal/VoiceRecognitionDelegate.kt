package io.pig.ui.reveal

import android.app.Activity
import android.content.Context
import kotlin.jvm.JvmOverloads
import io.pig.ui.reveal.VoiceRecognitionDelegate
import android.content.Intent
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

abstract class VoiceRecognitionDelegate {
    private val mVoiceRecognitionRequestCode: Int
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null

    constructor(activity: Activity, activityRequestCode: Int = DEFAULT_VOICE_REQUEST_CODE) {
        mActivity = activity
        mVoiceRecognitionRequestCode = activityRequestCode
    }

    constructor(fragment: Fragment, activityRequestCode: Int = DEFAULT_VOICE_REQUEST_CODE) {
        mFragment = fragment
        mVoiceRecognitionRequestCode = activityRequestCode
    }

    fun onStartVoiceRecognition() {
        if (mActivity != null) {
            val intent = buildVoiceRecognitionIntent()
            mActivity!!.startActivityForResult(intent, mVoiceRecognitionRequestCode)
        } else if (mFragment != null) {
            val intent = buildVoiceRecognitionIntent()
            mFragment!!.startActivityForResult(intent, mVoiceRecognitionRequestCode)
        }
    }

    protected val context: Context?
        protected get() = if (mActivity != null) {
            mActivity
        } else if (mFragment != null) {
            mFragment!!.context
        } else {
            throw IllegalStateException("Could not get context in VoiceRecognitionDelegate.")
        }

    abstract fun buildVoiceRecognitionIntent(): Intent
    abstract val isVoiceRecognitionAvailable: Boolean

    companion object {
        const val DEFAULT_VOICE_REQUEST_CODE = 8185102
    }
}