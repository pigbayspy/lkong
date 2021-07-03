package io.pig.lkong.ui.post.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.R
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityPostListBinding
import io.pig.lkong.ui.adapter.PostListAdapter
import io.pig.lkong.ui.adapter.listener.OnPostButtonClickListener
import javax.inject.Inject

class PostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostListBinding
    private lateinit var postListViewModel: PostListViewModel
    private lateinit var postListView: RecyclerView

    @Inject
    lateinit var userAccountManager: UserAccountManager

    private var threadId = -1L
    private var targetPostId = -1L
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postListViewModel = ViewModelProvider(this).get(PostListViewModel::class.java)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_post_list)

        // 自动注入
        injectThis()

        // 获取 View
        postListView = findViewById(R.id.recycle_list_post)

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
        initRecycleView()
    }

    private fun injectThis() {
        LkongApplication.get(this).presentComponent().inject(this)
    }

    private fun initRecycleView() {
        postListViewModel.postList.observe(this) {
            val userId = userAccountManager.getCurrentUserAccount().userId
            val listener = object : OnPostButtonClickListener {
                override fun onProfileImageClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onEditClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onPostTextLongClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onRateClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onRateTextClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onReplyClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onShareClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }
            }
            val adapter = PostListAdapter(this, userId, listener, it)
            postListView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            postListView.adapter = adapter
        }
        postListViewModel.getPost(threadId, currentPage)
    }
}