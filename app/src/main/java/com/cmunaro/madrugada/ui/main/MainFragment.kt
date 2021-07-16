package com.cmunaro.madrugada.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cmunaro.madrugada.R
import com.cmunaro.madrugada.databinding.MainFragmentBinding
import it.cmunaro.madrugada.MadrugadaFragment
import it.cmunaro.madrugada.pattern_matching.PatternMatchingConfig.Sequential
import kotlinx.coroutines.flow.collect

class MainFragment : MadrugadaFragment(R.layout.main_fragment) {
    override val viewModel: MainViewModel by viewModel()
    private val binding: MainFragmentBinding by viewBindingWithViewModelReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel(patternMatchingConfiguration = Sequential) {
            matchEvent(MainState::event) {
                Log.d("MainState", "match fired event")
            }
            matchPartial(MainState::counter) {
                Log.d("MainState", "match $it")
            }
            matchPartial(MainState::string) {
                Log.d("MainState", "match $it")
            }
            patternMatchFull(MainState::string) {
                Log.d("MainState", "match whole state from string $it")
            }
            matchPartial(
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