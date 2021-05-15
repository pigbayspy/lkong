package io.pig.lkong.ui.gallery

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.R
import io.pig.lkong.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var selfBinding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        selfBinding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = selfBinding.root

        // Todo 自定义 toolbar

        // 自定义 menu
        setHasOptionsMenu(true)

        val textView: TextView = selfBinding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gallery_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_gallery_evening) {
            // Todo
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 使用自定义标题栏
     */
    private fun initToolbar(toolbar: Toolbar, title: String, isDisplayHomeAsUp: Boolean) {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        val actionBar = appCompatActivity.supportActionBar
        if (actionBar != null) {
            actionBar.title = title
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp)
        }
    }
}