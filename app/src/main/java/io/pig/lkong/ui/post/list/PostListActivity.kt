package io.pig.lkong.ui.post.list

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import io.pig.ui.snakebar.SnakeBarType
import io.pig.ui.snakebar.showSnakeBar
import io.pig.widget.PagerControl
import io.pig.widget.listener.OnPagerControlListener
import io.pig.widget.util.QuickReturnHelper
import javax.inject.Inject
import kotlin.math.abs

class PostListActivity : AppCompatActivity(), Injectable {

    private val primaryColorInPostControl = Prefs.getBoolPrefs(
        PrefConst.USE_PRIMARY_COLOR_POST_CONTROL, PrefConst.USE_PRIMARY_COLOR_POST_CONTROL_VALUE
    )

    private val imageDownloadPolicy = Prefs.getStringPrefs(
        PrefConst.IMAGE_DOWNLOAD_POLICY, PrefConst.IMAGE_DOWNLOAD_POLICY_VALUE
    )

    private val scrollByVolumeKey =
        Prefs.getBoolPrefs(PrefConst.SCROLL_BY_VOLUME_KEY, PrefConst.SCROLL_BY_VOLUME_KEY_VALUE)

    private val pagerControlListener = object : OnPagerControlListener {
        override fun onBackwardClick() {
            postListViewModel.goToPrevPage()
        }

        override fun onForwardClick() {
            postListViewModel.goToNextPage()
        }

        override fun onPageIndicatorClick() {
            MaterialDialog(this@PostListActivity)
                .title(text = getString(R.string.dialog_post_list_choose_page))
                .listItemsSingleChoice(items = getPageIndicatorItems()) { _, index, _ ->
                    postListViewModel.goToPage(index + 1)
                }.show()
        }
    }

    private lateinit var binding: ActivityPostListBinding
    private lateinit var headerBinding: LayoutPostIntroHeaderBinding
    private lateinit var postListViewModel: PostListViewModel

    private lateinit var pageController: PagerControl
    private lateinit var fab: FloatingActionButton
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarReturnHelper: QuickReturnHelper

    private val mBaseTranslationY = 0

    @Inject
    lateinit var userAccountManager: UserAccountManager

    @Inject
    lateinit var lkongDataBase: LkongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostListBinding.inflate(layoutInflater)
        headerBinding = LayoutPostIntroHeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 自动注入
        injectThis()
        fab = binding.postListFab
        pageController = binding.postListPageControl.root
        toolbar = binding.postListToolbar
        toolbarReturnHelper = QuickReturnHelper(toolbar, QuickReturnHelper.ANIMATE_DIRECTION_UP)

        // 设置参数
        val thread = intent.getLongExtra(DataContract.BUNDLE_THREAD_ID, -1)
        val targetPostId = intent.getLongExtra(DataContract.BUNDLE_POST_ID, -1)
        if (thread == -1L && targetPostId == -1L) {
            throw IllegalStateException("PostListActivity missing extra in intent.")
        }
        val viewModelFactory = PostListViewModelFactory(thread)
        postListViewModel =
            ViewModelProvider(this, viewModelFactory).get(PostListViewModel::class.java)
        initRecycleView()
        initFab()
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
            postListViewModel.getPost()
        }
        source.addSource(postListViewModel.detail) {
            refreshPosts(it.thread.author.uid, it.posts)
            setThreadSubjectSpanned(it.thread)
        }
        source.observe(this) {
            updatePageText()
        }
        postListViewModel.initPage(1)
        binding.recycleListPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var dragging = false
            private var negativeDyAmount = 0
            private var amountScrollY = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                this.dragging = RecyclerView.SCROLL_STATE_DRAGGING == newState
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && isRecyclerViewAtBottom(recyclerView)
                ) {
                    pageController.show()
                    fab.show()
                    toolbarReturnHelper.show()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                amountScrollY += dy
                val toolbarHeight: Int = toolbar.height
                if (dy > 0) {
                    negativeDyAmount = 0
                    if (amountScrollY - mBaseTranslationY - toolbarHeight > toolbarHeight) {
                        toolbarReturnHelper.hide()
                        pageController.hide()
                        fab.hide()
                    }
                } else if (dy < 0) {
                    amountScrollY = 0
                    negativeDyAmount += dy
                    if (abs(negativeDyAmount - mBaseTranslationY) > toolbarHeight) {
                        toolbarReturnHelper.show()
                        pageController.show()
                        fab.show()
                    }
                }
            }
        })
    }

    private fun initPageController() {
        val postControlColor = if (primaryColorInPostControl.get()) {
            getPrimaryColor()
        } else {
            getAccentColor()
        }
        val toolbarTextColor = if (ThemeUtil.isColorLight(postControlColor)) {
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
        pageController.setOnPagerControlListener(pagerControlListener)
        pageController.getBackwardButton().setImageDrawable(backwardArrow)
        pageController.getForwardButton().setImageDrawable(forwardArrow)
        pageController.getContainer().setBackgroundColor(postControlColor)
    }

    private fun initFab() {
        val postControlColor = if (primaryColorInPostControl.get()) {
            getPrimaryColor()
        } else {
            getAccentColor()
        }
        val postControlColorDark = ThemeUtil.makeColorDarken(postControlColor, 0.8f)
        val postControlColorRipple = ThemeUtil.makeColorDarken(postControlColor, 0.9f)
        fab.setBackgroundColor(postControlColorDark)
        fab.rippleColor = postControlColorRipple
        fab.setOnClickListener {
            // Todo
        }
    }

    private fun getPageIndicatorItems(): List<CharSequence> {
        return List(postListViewModel.getPages()) {
            getString(
                R.string.format_post_list_page_indicator_detail,
                it + 1,
                it * PAGE_SIZE + 1,
                (it + 1) * PAGE_SIZE
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
                AppNavigation.openUserProfileActivity(this@PostListActivity, uid)
            }

            override fun onEditClick(view: View, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onPostTextLongClick(view: View, post: PostModel) {
                openContentDialog(post)
            }

            override fun onRateClick(view: View, pid: String, tid: Long) {
                openRateDialog(pid)
            }

            override fun onRateTextClick(view: View, rates: List<PostModel.PostRateModel>) {
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

    private fun openRateLogDialog(rates: List<PostModel.PostRateModel>) {
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

    private fun openRateDialog(pid: String) {
        MaterialDialog(this).show {
            title(R.string.dialog_title_rate)
            customView(R.layout.dialog_input_rate, scrollable = false)
            positiveButton(android.R.string.ok, click = {
                val reasonEditText = it.findViewById<EditText>(R.id.edit_reason)
                val numEditText = it.findViewById<EditText>(R.id.edit_num)
                val reason = reasonEditText.text.toString()
                val numText = numEditText.text.toString()
                if (TextUtils.isEmpty(numText) || !TextUtils.isDigitsOnly(numText) || numText == "0") {
                    showSnakeBar(
                        binding.root,
                        getString(R.string.toast_error_rate_score_empty),
                        SnakeBarType.ERROR
                    )
                    return@positiveButton
                }
                val num = Integer.parseInt(numText)
                postListViewModel.ratePost(
                    pid,
                    num,
                    reason,
                    binding.recycleListPost.adapter as Bookends
                )
            })
        }
    }

    private fun setThreadSubjectSpanned(thread: PostRespThreadData) {
        val spannableTitle = SpannableStringBuilder()
        if (!thread.digest.isNullOrEmpty()) {
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
        pageController.setPageIndicatorText(
            getString(
                R.string.format_post_list_page_indicator,
                page,
                pages
            )
        )
    }

    private fun isRecyclerViewAtBottom(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager
        val adapter = recyclerView.adapter!!
        return when (layoutManager) {
            is GridLayoutManager -> {
                layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1
            }
            is LinearLayoutManager -> {
                layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1
            }
            else -> {
                throw IllegalStateException()
            }
        }
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

    private fun setLoading(isLoading: Boolean) {
        binding.postListLoading.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    companion object {
        /**
         * 一页的长度
         */
        const val PAGE_SIZE = 20
    }
}