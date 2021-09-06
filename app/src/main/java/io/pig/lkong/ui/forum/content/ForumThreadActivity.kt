package io.pig.lkong.ui.forum.content

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.pig.common.ui.adapter.Bookends
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityForumThreadBinding
import io.pig.lkong.model.listener.ForumThreadModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.ForumThreadAdapter
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.getAccentColor
import io.pig.ui.common.isActivityDestroyed

class ForumThreadActivity : AppCompatActivity() {

    private val listener = object : OnThreadClickListener {
        override fun onItemThreadClick(view: View, tid: Long) {
            AppNavigation.openPostListActivity(this@ForumThreadActivity, tid)
        }

        override fun onProfileAreaClick(view: View, uid: Long) {
            AppNavigation.openUserProfileActivity(this@ForumThreadActivity, uid)
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

        // get param
        val fid = intent.getLongExtra(DataContract.BUNDLE_FORUM_ID, -1)

        // set title
        val forumName = intent.getStringExtra(DataContract.BUNDLE_FORUM_NAME) ?: ""
        if (forumName.isNotBlank()) {
            title = forumName
        }

        val viewModelFactory = ForumThreadViewModelFactory(fid, forumName)
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            ForumThreadViewModel::class.java
        )
        binding = ActivityForumThreadBinding.inflate(layoutInflater)

        val root = binding.root

        setContentView(root)

        initRecycle()
        initFab()
        initHeader()
        initProcessBar()

        viewModel.threads.observe(this) {
            refresh(it)
        }
        viewModel.getThreads()
    }

    private fun refresh(threads: List<ForumThreadModel>) {
        threadAdapter.submitList(threads)
    }

    private fun initRecycle() {
        binding.forumThreadList.apply {
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
            AppNavigation.openNewThreadActivity(this, viewModel.forumId, viewModel.forumName)
        }
        val accentColor = getAccentColor()
        val accentColorDark = ThemeUtil.makeColorDarken(accentColor, 0.8f)
        val accentColorRipple = ThemeUtil.makeColorDarken(accentColor, 0.9f)
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_button_edit, null)!!.mutate()
        val tint = if (ThemeUtil.isColorLight(accentColor)) {
            Color.BLACK
        } else {
            Color.WHITE
        }
        DrawableCompat.setTint(drawable, tint)
        val backgroundDrawable = StateListDrawable()
        backgroundDrawable.apply {
            addState(intArrayOf(android.R.attr.state_pressed), createDrawable(accentColorDark))
            addState(intArrayOf(), createDrawable(accentColor))
        }
        binding.forumThreadFab.apply {
            setImageDrawable(drawable)
            rippleColor = accentColorRipple
            background = backgroundDrawable
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

    private fun createDrawable(color: Int): Drawable {
        val ovalShape = OvalShape()
        val shapeDrawable = ShapeDrawable(ovalShape)
        shapeDrawable.paint.color = color
        return shapeDrawable
    }
}