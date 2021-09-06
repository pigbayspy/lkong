package io.pig.ui.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.pig.lkong.R

abstract class SimpleContainerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_container)
        setUpFragment()
    }

    private fun setUpFragment() {
        val fragmentTransaction = supportFragmentManager
            .beginTransaction()
        fragmentTransaction.replace(R.id.container, newFragment())
        fragmentTransaction.commit()
    }

    protected abstract fun newFragment(): Fragment
}