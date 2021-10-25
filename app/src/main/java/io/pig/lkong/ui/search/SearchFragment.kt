package io.pig.lkong.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.FragmentSearchBinding

class SearchFragment(private val listener: OnSearchResultListener) : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    companion object {
        fun newInstance(searchText: String, listener: OnSearchResultListener): SearchFragment {
            val fragment = SearchFragment(listener)
            val args = bundleOf(DataContract.BUNDLE_SEARCH_QUERY to searchText)
            fragment.arguments = args
            return fragment
        }
    }
}