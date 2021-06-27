package com.cmunaro.madrugada.ui.page_one

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cmunaro.madrugada.R
import com.cmunaro.madrugada.databinding.PageOneFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PageOneFragment : Fragment(R.layout.page_one_fragment) {
    private lateinit var binding: PageOneFragmentBinding
    private val viewModel: PageOneViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding =  DataBindingUtil.bind(view)!!

        binding.button.setOnClickListener { viewModel.increment() }

        viewModel.counter.observe(viewLifecycleOwner) {
            binding.button.text = it.toString()
        }
    }
}