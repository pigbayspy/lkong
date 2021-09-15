package io.pig.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import io.pig.lkong.R
import io.pig.widget.listener.RecycleViewClickListener
import java.util.function.Consumer

class EmoticonDialog : DialogFragment() {

    private lateinit var callback: Consumer<String>
    private lateinit var emoticonCollectionView: RecyclerView
    private lateinit var emoticonFileNames: List<String>
    private lateinit var collectionAdapter: EmoticonAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialDialog(requireContext())
            .title(R.string.dialog_choose_emoticon_title)
            .customView(R.layout.dialog_emoticon, scrollable = false)
            .noAutoDismiss()
        emoticonCollectionView = dialog.getCustomView().findViewById(R.id.dialog_emoticon_recycle)
        emoticonFileNames = listAssetFiles("emoji")
        emoticonFileNames.sortedBy {
            val dotIndex = it.indexOf(".")
            it.substring(2, dotIndex).toInt()
        }

        emoticonCollectionView.itemAnimator = DefaultItemAnimator()
        emoticonCollectionView.layoutManager = GridLayoutManager(activity, 5)
        val listener = object : RecycleViewClickListener {
            override fun onItemClick(view: View, pos: Int, id: Long) {
                callback.accept(collectionAdapter.getItem(pos))
                dismiss()
            }
        }
        collectionAdapter = EmoticonAdapter(requireContext(), emoticonFileNames, listener)
        emoticonCollectionView.adapter = collectionAdapter
        return dialog
    }

    private fun listAssetFiles(path: String): List<String> {
        try {
            val fileNameArr = resources.assets.list(path)
            if (fileNameArr.isNullOrEmpty()) {
                return emptyList()
            }
            return fileNameArr.toList()
        } catch (e: Exception) {
            return emptyList()
        }
    }

    fun show(context: AppCompatActivity, callback: Consumer<String>) {
        this.callback = callback
        super.show(context.supportFragmentManager, "EMOTICON_SELECTOR")
    }
}