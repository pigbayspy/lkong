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
import io.pig.common.ui.adapter.Bookends
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityForumThreadBinding
import io.pig.lkong.model.listener.ForumThreadModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.ForumThreadAdapter
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.ui.common.isActivityDestroyed

class ForumThreadActivity : AppCompatActivity() {

    private val listener = object : OnThreadClickListener {
        override fun onItemThreadClick(view: View, tid: Long) {
            AppNavigation.openPostListActivity(this@ForumThreadActivity, tid)
        }

        override fun onProfileAreaClick(view: View, uid: Long) {
            AppNavigation.openActivityForUserProfile(this@ForumThreadActivity, uid)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ForumThreadViewModel::class.java)
        binding = ActivityForumThreadBinding.inflate(layoutInflater)

        val root = binding.root

        // get param
        val fid = intent.getLongExtra(DataContract.BUNDLE_FORUM_ID, -1)
        val avatar = intent.getStringExtra(DataContract.BUNDLE_FORUM_AVATAR) ?: ""

        // set title
        val forumName = intent.getStringExtra(DataContract.BUNDLE_FORUM_NAME)
        if (!forumName.isNullOrBlank()) {
            title = forumName
        }

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
        binding.forumThreadList.apply {
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
        binding.forumThreadFab.setOnClickListener {
            // Todo
        }
    }

    private fun initHeader() {
        // bookend
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