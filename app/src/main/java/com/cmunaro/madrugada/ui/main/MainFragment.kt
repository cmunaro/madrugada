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
                Log.d("MainState", "match $it")
            }
            patternMatchPartialStateChange(MainState::string) {
                Log.d("MainState", "match $it")
            }
            patternMatchStateChange(MainState::string) {
                Log.d("MainState", "match whole state $it")
            }
            patternMatchPartialStateChange(
                MainState::counter,
                MainState::string
            ) { counter, string ->
                Log.d("MainState", "match $counter, $string")
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