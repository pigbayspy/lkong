package io.pig.lkong.ui.post.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityPostListBinding

class PostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostListBinding
    private lateinit var postListViewModel: PostListViewModel

    private var threadId = -1L
    private var targetPostId = -1L
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postListViewModel = ViewModelProvider(this).get(PostListViewModel::class.java)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_post_list)

        // 设置参数
        if (intent.hasExtra(DataContract.BUNDLE_THREAD_ID)) {
            threadId = intent.getLongExtra(DataContract.BUNDLE_THREAD_ID, threadId)
        } else if (intent.hasExtra(DataContract.BUNDLE_POST_ID)) {
            targetPostId = intent.getLongExtra(DataContract.BUNDLE_POST_ID, targetPostId)
        }
        currentPage = intent.getIntExtra(DataContract.BUNDLE_THREAD_CURRENT_PAGE, currentPage)
        if (threadId == -1L && targetPostId == -1L) {
            throw IllegalStateException("PostListActivity missing extra in intent.")
        }

    }
}