package io.pig.lkong.ui.post.list

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import io.pig.common.ui.adapter.Bookends
import io.pig.lkong.R
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.databinding.ActivityPostListBinding
import io.pig.lkong.databinding.LayoutPostIntroHeaderBinding
import io.pig.lkong.http.data.resp.data.PostRespThreadData
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

    private val primaryColorInPostControl = Prefs.getBoolPrefs(
        PrefConst.USE_PRIMARY_COLOR_POST_CONTROL, PrefConst.USE_PRIMARY_COLOR_POST_CONTROL_VALUE
    )

    private val imageDownloadPolicy = Prefs.getStringPrefs(
        PrefConst.IMAGE_DOWNLOAD_POLICY, PrefConst.IMAGE_DOWNLOAD_POLICY_VALUE
    )

    private val scrollByVolumeKey =
        Prefs.getBoolPrefs(PrefConst.SCROLL_BY_VOLUME_KEY, PrefConst.SCROLL_BY_VOLUME_KEY_VALUE)

    private lateinit var binding: ActivityPostListBinding
    private lateinit var headerBinding: LayoutPostIntroHeaderBinding
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
        headerBinding = LayoutPostIntroHeaderBinding.inflate(layoutInflater)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val isFlipPageByVolumeKey = scrollByVolumeKey.get()
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (isFlipPageByVolumeKey) {
                    scrollDown()
                    return true
                }
                return false
            }
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (isFlipPageByVolumeKey) {
                    scrollUp()
                    return true
                }
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
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
            setThreadSubjectSpanned(it.thread)
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
        val themeKey = getThemeKey()
        val userId = userAccountManager.getCurrentUserAccount().userId
        val display = windowManager.defaultDisplay
        val imagePolicy = imageDownloadPolicy.get().toInt()
        val adapter = PostListAdapter(
            context = this,
            userId = userId,
            authorId = authorId,
            display = display,
            listener = listener,
            imageDownloadPolicy = imagePolicy,
            themeKey = themeKey,
            postList = post
        )
        val wrapperAdapter = Bookends(adapter)
        // init header
        val headerLayoutParam = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val rootCard = headerBinding.root
        rootCard.layoutParams = headerLayoutParam
        rootCard.setBackgroundColor(ThemeUtil.textColorPrimaryInverse(this, themeKey))
        binding.recycleListPost.layoutManager = LinearLayoutManager(this)
        wrapperAdapter.addHeader(rootCard)
        binding.recycleListPost.adapter = wrapperAdapter
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

    private fun setThreadSubjectSpanned(thread: PostRespThreadData) {
        val spannableTitle = SpannableStringBuilder()
        if (thread.digest == true) {
            val digestIndicator = getString(R.string.indicator_thread_digest)
            spannableTitle.append(digestIndicator)
            spannableTitle.setSpan(
                ForegroundColorSpan(getAccentColor()),
                0,
                digestIndicator.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        spannableTitle.append(Html.fromHtml(thread.title, HtmlCompat.FROM_HTML_MODE_LEGACY))
        headerBinding.postHeaderTextTitle.text = spannableTitle
        val detailCount = getString(
            R.string.format_post_header_detail_count,
            thread.views,
            thread.replies
        )
        headerBinding.postHeaderTextDetailCount.text = detailCount
        headerBinding.postHeaderTextForumName.text = thread.forum.name
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

    private fun scrollDown() {
        binding.recycleListPost.smoothScrollBy(0, calcScrollDistance())
    }

    private fun scrollUp() {
        binding.recycleListPost.smoothScrollBy(0, -calcScrollDistance())
    }

    private fun calcScrollDistance(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.heightPixels * 3 / 5
    }

    companion object {
        /**
         * 一页的长度
         */
        const val PAGE_SIZE = 20
    }
}