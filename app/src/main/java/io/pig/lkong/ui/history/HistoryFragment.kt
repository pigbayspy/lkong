package io.pig.lkong.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var selfBinding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        selfBinding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = selfBinding.root

        val textView: TextView = selfBinding.textSlideshow
        historyViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}