package com.cmunaro.madrugada.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cmunaro.madrugada.R
import com.cmunaro.madrugada.base.MadrugadaFragment
import com.cmunaro.madrugada.databinding.MainFragmentBinding
import kotlinx.coroutines.flow.collect

class MainFragment : MadrugadaFragment(R.layout.main_fragment) {
    override val viewModel: MainViewModel by viewModel()
    private val binding: MainFragmentBinding by viewBindingWithViewModelReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel {
            patternMatchPartialStateEvent(MainState::event) {
                Log.d("MainState", "match fired event")
            }
            patternMatchPartialStateChange(MainState::counter) {
                Log.d("MainState", "match $it")
            }
            patternMatchPartialStateChange(MainState::string) {
                Log.d("MainState", "match $it")
            }
            patternMatchFullStateChange(MainState::string) {
                Log.d("MainState", "match whole state from string $it")
            }
            patternMatchPartialStateChange(
                MainState::counter,
                MainState::string
            ) { counter, string ->
                Log.d("MainState", "match $counter, $string")
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                Log.d("MainState", "Collected")
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