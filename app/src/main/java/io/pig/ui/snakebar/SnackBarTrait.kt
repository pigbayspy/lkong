package io.pig.ui.snakebar

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnakeBar(rootView: View, text: CharSequence, type: SnakeBarType) {
    SnakeBarUtil.makeSimple(
        rootView,
        text,
        type,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Fragment.showSnakeBar(text: CharSequence, type: SnakeBarType) {
    SnakeBarUtil.makeSimple(
        requireView(),
        text,
        type,
        Snackbar.LENGTH_SHORT
    ).show()
}