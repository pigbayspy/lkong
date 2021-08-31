package io.pig.lkong.ui.post.list

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import io.pig.lkong.R
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.databinding.ActivityPostListBinding
import io.pig.lkong.model.PostModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.PrefConst
import io.pig.lkong.preference.Prefs
import io.pig.lkong.ui.adapter.PostListAdapter
import io.pig.lkong.ui.adapter.PostRateAdapter
import io.pig.lkong.ui.adapter.listener.OnPostButtonClickListener
import io.pig.lkong.ui.common.Injectable
import io.pig.lkong.util.SlateUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.getAccentColor
import io.pig.ui.common.getPrimaryColor
import io.pig.ui.common.getThemeKey
import javax.inject.Inject

class PostListActivity : AppCompatActivity(), Injectable {

    private val primaryColorInPostControl by lazy {
        Prefs.getBoolPrefs(
            PrefConst.USE_PRIMARY_COLOR_POST_CONTROL,
            PrefConst.USE_PRIMARY_COLOR_POST_CONTROL_VALUE
        )
    }

    private lateinit var binding: ActivityPostListBinding
    private lateinit var postListViewModel: PostListViewModel

    @Inject
    lateinit var userAccountManager: UserAccountManager

    @Inject
    lateinit var lkongDataBase: LkongDatabase

    private var threadId = -1L
    private var targetPostId = -1L

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
        if (threadId == -1L && targetPostId == -1L) {
            throw IllegalStateException("PostListActivity missing extra in intent.")
        }
        initRecycleView()
        initPageController()
    }

    override fun injectThis() {
        LkongApplication.get(this).presentComponent().inject(this)
    }

    private fun initRecycleView() {
        val source = MediatorLiveData<Any>()
        source.addSource(postListViewModel.page) {
            postListViewModel.getPost(threadId)
        }
        source.addSource(postListViewModel.detail) {
            refreshPosts(it.thread.author.uid, it.posts)
        }
        source.observe(this) {
            updatePageText()
        }
        postListViewModel.initPage(1)
    }

    private fun initPageController() {
        val postControlColor =
            if (primaryColorInPostControl.get()) {
                getPrimaryColor()
            } else {
                getAccentColor()
            }
        val toolbarTextColor =
            if (ThemeUtil.isColorLight(postControlColor)) {
                Color.BLACK
            } else {
                Color.WHITE
            }
        val backwardArrow =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_backward, null)!!.mutate()
        val forwardArrow =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_forward, null)!!.mutate()
        ThemeUtil.setTint(backwardArrow, toolbarTextColor)
        ThemeUtil.setTint(forwardArrow, toolbarTextColor)
        binding.postListPageControl.pagerControlButtonBackward.apply {
            setImageDrawable(backwardArrow)
            setOnClickListener {
                postListViewModel.goToPrevPage()
            }
        }
        binding.postListPageControl.pagerControlButtonForward.apply {
            setImageDrawable(forwardArrow)
            setOnClickListener {
                postListViewModel.goToNextPage()
            }
        }
        binding.postListPageControl.pagerControlButtonPageIndicator.apply {
            setBackgroundColor(postControlColor)
            setOnClickListener {
                MaterialDialog(this@PostListActivity)
                    .title(text = getString(R.string.dialog_post_list_choose_page))
                    .listItemsSingleChoice(items = getPageIndicatorItems()) { _, index, _ ->
                        postListViewModel.goToPage(index + 1)
                    }
                    .show()
            }
        }
    }

    private fun getPageIndicatorItems(): List<CharSequence> {
        return List(postListViewModel.getPages()) {
            getString(
                R.string.format_post_list_page_indicator_detail,
                it,
                (it - 1) * PAGE_SIZE + 1,
                it * PAGE_SIZE
            )
        }
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

    private fun refreshPosts(authorId: Long, post: List<PostModel>) {
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
        val userId = userAccountManager.getCurrentUserAccount().userId
        val display = windowManager.defaultDisplay
        val adapter =
            PostListAdapter(this, userId, authorId, display, listener, getThemeKey(), post)
        binding.recycleListPost.layoutManager = LinearLayoutManager(this)
        binding.recycleListPost.adapter = adapter
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

    /**
     * 渲染页数
     */
    private fun updatePageText() {
        val page = postListViewModel.getPage()
        val pages = postListViewModel.getPages()
        binding.postListPageControl.pagerControlButtonPageIndicator.text =
            getString(R.string.format_post_list_page_indicator, page, pages)
    }

    companion object {
        /**
         * 一页的长度
         */
        const val PAGE_SIZE = 20
    }
}