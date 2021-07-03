package io.pig.lkong.ui.thread.digest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.pig.lkong.R

/**
 * 精华
 */
class DigestThreadFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digest_thread, container, false)
    }

    companion object {

        fun newInstance() = DigestThreadFragment()
    }
}