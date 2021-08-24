package io.pig.lkong.ui.post.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import io.pig.lkong.R
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.databinding.ActivityPostListBinding
import io.pig.lkong.model.PostModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.PostListAdapter
import io.pig.lkong.ui.adapter.PostRateAdapter
import io.pig.lkong.ui.adapter.listener.OnPostButtonClickListener
import io.pig.lkong.ui.common.Injectable
import io.pig.lkong.util.SlateUtil
import javax.inject.Inject

class PostListActivity : AppCompatActivity(), Injectable {

    private lateinit var binding: ActivityPostListBinding
    private lateinit var postListViewModel: PostListViewModel

    @Inject
    lateinit var userAccountManager: UserAccountManager

    @Inject
    lateinit var lkongDataBase: LkongDatabase

    private var threadId = -1L
    private var targetPostId = -1L
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postListViewModel = ViewModelProvider(this).get(PostListViewModel::class.java)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 自动注入
        injectThis()

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

    override fun injectThis() {
        LkongApplication.get(this).presentComponent().inject(this)
    }

    private fun initRecycleView() {
        postListViewModel.postList.observe(this) {
            val userId = userAccountManager.getCurrentUserAccount().userId
            val listener = object : OnPostButtonClickListener {
                override fun onProfileImageClick(view: View, uid: Long) {
                    AppNavigation.openActivityForUserProfile(this@PostListActivity, uid)
                }

                override fun onEditClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onPostTextLongClick(view: View, post: PostModel) {
                    openContentDialog(post)
                }

                override fun onRateClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onRateTextClick(view: View, rates: List<PostModel.PostRate>) {
                    openRateLogDialog(rates)
                }

                override fun onReplyClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }

                override fun onShareClick(view: View, position: Int) {
                    TODO("Not yet implemented")
                }
            }
            val adapter = PostListAdapter(this, userId, listener, it)
            binding.recycleListPost.layoutManager =
                LinearLayoutManager(this)
            binding.recycleListPost.adapter = adapter
        }
        postListViewModel.getPost(threadId, currentPage)
    }

    override fun onStop() {
        super.onStop()
        val userId = userAccountManager.getCurrentUserAccount().userId
        // get middle item
        val layoutMgr = binding.recycleListPost.layoutManager as LinearLayoutManager
        val middlePos =
            (layoutMgr.findFirstVisibleItemPosition() + layoutMgr.findLastVisibleItemPosition()) / 2
        postListViewModel.saveHistory(lkongDataBase, userId, middlePos)
    }

    private fun openRateLogDialog(rates: List<PostModel.PostRate>) {
        val adapter = PostRateAdapter(this, rates)
        MaterialDialog(this).apply {
            title(R.string.dialog_title_rate)
            customListAdapter(adapter)
            positiveButton(android.R.string.ok)
            show()
        }
    }

    private fun openContentDialog(post: PostModel) {
        if (!post.message.isNullOrBlank()) {
            MaterialDialog(this).apply {
                title(R.string.dialog_title_copy_content)
                message(text = SlateUtil.slateToText(post.message))
                show()
            }
        }
    }
}