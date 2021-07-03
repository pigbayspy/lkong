package io.pig.lkong.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.pig.lkong.R

/**
 * 板块列表
 *
 * @author yinhang
 */
class ForumsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forums, container, false)
    }

    companion object {
        fun newInstance() = ForumsFragment()
    }
}