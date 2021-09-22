package io.pig.lkong.ui.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding

class PmFragment : Fragment() {

    private lateinit var binding: LayoutSimpleRecycleBinding
    private lateinit var pmViewModel: PmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pmViewModel = ViewModelProvider(this).get(PmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    companion object {
        fun newInstance() = PmFragment()
    }
}