package io.pig.lkong.ui.forum.content

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityForumThreadBinding
import io.pig.lkong.model.listener.ForumThreadModel
import io.pig.lkong.ui.adapter.ForumThreadAdapter
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.ui.common.isActivityDestroyed
import io.pig.common.ui.adapter.Bookends

class ForumThreadActivity : AppCompatActivity() {

    private val listener = object : OnThreadClickListener {
        override fun onItemThreadClick(view: View, pos: Int) {
            TODO("Not yet implemented")
        }

        override fun onProfileAreaClick(view: View, pos: Int, uid: Long) {
            TODO("Not yet implemented")
        }
    }

    private val threadAdapter by lazy {
        ForumThreadAdapter(this, listener)
    }

    private val wrapperAdapter by lazy {
        Bookends(threadAdapter)
    }

    private val progressBar by lazy {
        ProgressBar(this)
    }

    private lateinit var binding: ActivityForumThreadBinding
    private lateinit var viewModel: ForumThreadViewModel

    private var fid: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ForumThreadViewModel::class.java)
        binding = ActivityForumThreadBinding.inflate(layoutInflater)

        // get param
        fid = intent.getLongExtra(DataContract.BUNDLE_FORUM_ID, -1)
        val forumName = intent.getStringExtra(DataContract.BUNDLE_FORUM_NAME)
        if (!forumName.isNullOrBlank()) {
            title = forumName
        }

        val root = binding.root
        setContentView(root)

        initRecycle()
        initFab()
        initHeader()
        initProcessBar()

        viewModel.threads.observe(this) {
            refresh(it)
        }
        viewModel.getThreads(fid)
    }

    private fun refresh(threads: List<ForumThreadModel>) {
        threadAdapter.submitList(threads)
    }

    private fun initRecycle() {
        binding.activityForumThreadList.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = wrapperAdapter
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@ForumThreadActivity)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!isActivityDestroyed()) {
                            Glide.with(this@ForumThreadActivity).resumeRequests()
                        }
                    } else {
                        Glide.with(this@ForumThreadActivity).pauseRequests()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun initFab() {
    }

    private fun initHeader() {
        val headerView = layoutInflater.inflate(R.layout.layout_forum_header, binding.root, false)
        val typeSpinner =
            headerView.findViewById<Spinner>(R.id.layout_forum_header_spinner_list_type)
        val typeNames = resources.getStringArray(R.array.thread_list_type_arrays)
        val typeArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, typeNames)
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeArrayAdapter
        val headerLayoutParam = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        headerView.layoutParams = headerLayoutParam
        // add header
        wrapperAdapter.addHeader(headerView)
    }

    private fun initProcessBar() {
        val footerLayoutParam = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        progressBar.layoutParams = footerLayoutParam
        progressBar.visibility = View.INVISIBLE
        wrapperAdapter.addFooter(progressBar)
    }

    private fun processOnMore() {

    }
}