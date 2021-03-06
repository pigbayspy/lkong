package io.pig.lkong.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import io.pig.lkong.R
import io.pig.lkong.util.TextSizeUtil
import java.util.*

class TextSizeDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialDialog(requireContext())
            .customView(R.layout.layout_dialog_text_size, scrollable = true)
            .title(text = requireArguments().getString(KEY_TITLE))
            .positiveButton(android.R.string.ok)
            .negativeButton(android.R.string.cancel)
            .neutralButton(R.string.setting_theme_default_value)
            .noAutoDismiss()

        val view: View = dialog.getCustomView()
        val mPreview = view.findViewById<View>(R.id.preview) as TextView
        val seekBar = view.findViewById<View>(R.id.seeker) as SeekBar
        val mValue = view.findViewById<View>(R.id.value) as TextView

        var mode = requireArguments().getString(KEY_MODE)
        if (mode != null) {
            mode = mode.substring(mode.indexOf('|') + 1)
        }

        val defaultValue: Int = TextSizeUtil.textSizeForMode(
            requireActivity(),
            requireArguments().getString(KEY_THEME_KEY)!!, mode!!
        )
        mPreview.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultValue.toFloat())
        seekBar.max = seekerMax(mode)
        val dpValue: Int = TextSizeUtil.pxToSp(this, defaultValue)
        seekBar.progress = dpValue - 1
        mValue.text = String.format(Locale.getDefault(), "%dsp", dpValue)
        return dialog
    }

    private fun seekerMax(mode: String): Int {
        return when (mode) {
            TextSizeUtil.TEXT_SIZE_CAPTION -> 32
            TextSizeUtil.TEXT_SIZE_BODY -> 32
            TextSizeUtil.TEXT_SIZE_SUBHEADING -> 32
            TextSizeUtil.TEXT_SIZE_TITLE -> 48
            TextSizeUtil.TEXT_SIZE_HEADLINE -> 48
            TextSizeUtil.TEXT_SIZE_DISPLAY1 -> 48
            TextSizeUtil.TEXT_SIZE_DISPLAY2 -> 48
            TextSizeUtil.TEXT_SIZE_DISPLAY3 -> 48
            TextSizeUtil.TEXT_SIZE_DISPLAY4 -> 48
            else -> 32
        }
    }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_MODE = "text_size_mode"
        private const val KEY_THEME_KEY = "theme_key"
        private const val KEY_RECREATE = "recreate_on_apply"
        private const val TAG = "[text-size-dialog]"

        fun show(
            context: FragmentActivity, textSizeMode: String,
            themeKey: String, title: CharSequence, recreateOnApply: Boolean
        ) {
            val dialog = TextSizeDialog()
            dialog.arguments = bundleOf(
                KEY_MODE to textSizeMode,
                KEY_THEME_KEY to themeKey,
                KEY_TITLE to title as String,
                KEY_RECREATE to recreateOnApply
            )
            dialog.show(context.supportFragmentManager, TAG)
        }
    }
}