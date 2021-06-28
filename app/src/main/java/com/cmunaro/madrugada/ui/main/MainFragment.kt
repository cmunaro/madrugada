package com.cmunaro.madrugada.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.cmunaro.madrugada.R
import com.cmunaro.madrugada.base.MadrugadaFragment
import com.cmunaro.madrugada.databinding.MainFragmentBinding

class MainFragment : MadrugadaFragment(R.layout.main_fragment) {
    override val viewModel: MainViewModel by viewModel()
    private val binding: MainFragmentBinding by viewBindingWithViewModelReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel {
            patternMatchPartialStateChange(MainState::counter) {
                Log.d("MainState", "match counter $it")
            }
            patternMatchPartialStateChange(MainState::string) {
                Log.d("MainState", "match string $it")
            }
            patternMatchPartialStateChange(MainState::counter, MainState::string) {
                Log.d("MainState", "match counter string $it")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            val direction = MainFragmentDirections.actionMainFragmentToPageOne()
            findNavController().navigate(direction)
        }
    }
}