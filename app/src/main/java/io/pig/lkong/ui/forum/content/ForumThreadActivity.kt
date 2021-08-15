package io.pig.lkong.ui.forum.content

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityForumThreadBinding
import io.pig.lkong.model.listener.ForumThreadModel
import io.pig.lkong.ui.adapter.ForumThreadAdapter
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener

class ForumThreadActivity : AppCompatActivity() {

    private val listener = object : OnThreadClickListener {
        override fun onItemThreadClick(view: View, pos: Int) {
            TODO("Not yet implemented")
        }

        override fun onProfileAreaClick(view: View, pos: Int, uid: Long) {
            TODO("Not yet implemented")
        }
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

        initFab()

        viewModel.threads.observe(this) {
            refresh(it)
        }
        viewModel.getThreads(fid)
    }

    private fun refresh(threads: List<ForumThreadModel>) {
        binding.activityForumThreadList.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = ForumThreadAdapter(this@ForumThreadActivity, listener, threads)
        }
    }

    private fun initFab() {
    }
}