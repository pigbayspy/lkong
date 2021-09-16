package io.pig.lkong.ui.edit

import android.graphics.drawable.Drawable
import android.widget.EditText
import io.pig.widget.html.AsyncDrawableType
import io.pig.widget.html.ImageSpanContainer
import java.lang.ref.WeakReference

class ImageSpanContainerImpl(editText: EditText) : ImageSpanContainer {

    private val mEditText: WeakReference<EditText> = WeakReference(editText)

    override fun notifyImageSpanLoaded(tag: Any, drawable: Drawable, type: AsyncDrawableType) {
        mEditText.get()?.invalidate()
    }
}