package io.pig.lkong.ui.edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import io.pig.lkong.R
import io.pig.lkong.bridge.HttpClient
import io.pig.lkong.databinding.ActivityNewThreadBinding
import io.pig.lkong.preference.PrefConst
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.ui.common.AbstractActivity

abstract class AbstractPostActivity : AbstractActivity() {

    protected lateinit var binding: ActivityNewThreadBinding

    private lateinit var postTailText: StringPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initConfig()
        initToolbar()
        initWebView()

        title = getString(R.string.button_edit)
    }

    private fun initWebView() {
        val webView = binding.threadEditContent
        val setting = webView.settings
        setting.cacheMode = WebSettings.LOAD_NO_CACHE
        setting.javaScriptEnabled = true
        webView.addJavascriptInterface(HttpClient(), "httpClient")
        webView.loadUrl("")
    }

    private fun initToolbar() {
        val toolbar = binding.newThreadToolbar
        toolbar.setBackgroundColor(getPrimaryColor())
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_post -> {
                return true
            }
            R.id.action_change_theme -> {
                toggleNightMode()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initConfig() {
        postTailText = Prefs.getStringPrefs(
            PrefConst.POST_TAIL_TEXT,
            PrefConst.POST_TAIL_TEXT_VALUE
        )
    }

    companion object {
        private const val PICASSO_TAG = "abstract_post_activity"
    }
}