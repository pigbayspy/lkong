package io.pig.lkong.ui.pm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityPrivateChatBinding
import io.pig.lkong.model.PrivateMessageModel
import io.pig.lkong.ui.adapter.PmAdapter
import io.pig.lkong.ui.notify.PmFragment
import io.pig.ui.common.getPrimaryColor

class PmActivity : AppCompatActivity() {

    private lateinit var listAdapter: PmAdapter
    private lateinit var binding: ActivityPrivateChatBinding
    private lateinit var pmViewModel: PmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getLongExtra(DataContract.BUNDLE_USER_ID, -1L)
        val username = intent.getStringExtra(DataContract.BUNDLE_USER_NAME) ?: ""
        val userAvatar = intent.getStringExtra(DataContract.BUNDLE_USER_AVATAR)
        binding = ActivityPrivateChatBinding.inflate(layoutInflater)
        val root = binding.root

        val viewModelFactory = PmViewModelFactory(userId, userAvatar)
        pmViewModel = ViewModelProvider(this, viewModelFactory).get(PmViewModel::class.java)
        listAdapter = PmAdapter(this, userId, userAvatar)
        setContentView(root)
        initToolbar(username)
        initRecycleView()
    }

    private fun initToolbar(username: String) {
        val toolbar = binding.privateMessageToolbar
        toolbar.setBackgroundColor(getPrimaryColor())
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = username
    }

    private fun initRecycleView() {
        pmViewModel.apply {
            privateMessages.observe(this@PmActivity) {
                refreshPrivateMsg(it)
            }
            loading.observe(this@PmActivity) {
                refreshLoading(it)
            }
        }
        pmViewModel.getPrivateMsgs()
        binding.pmSwipeLayout.setOnRefreshListener {
            pmViewModel.refresh()
        }
        val layoutMgr = LinearLayoutManager(this)
        layoutMgr.stackFromEnd = true
        binding.pmRecyclerMessages.apply {
            layoutManager = layoutMgr
            adapter = listAdapter
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastPos = layoutMgr.findLastCompletelyVisibleItemPosition()
                    if (dy > 0 && layoutMgr.itemCount - lastPos <= TO_LAST_LEFT) {
                        // load more
                        pmViewModel.getPrivateMsgs()
                    }
                }
            })
        }
    }

    private fun refreshPrivateMsg(pmUsers: List<PrivateMessageModel>) {
        listAdapter.submitList(pmUsers)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.pmSwipeLayout.isRefreshing = loading
    }

    companion object {
        private const val TO_LAST_LEFT = 5
    }
}