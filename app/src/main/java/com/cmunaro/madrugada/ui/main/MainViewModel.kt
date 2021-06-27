package com.cmunaro.madrugada.ui.main

import androidx.lifecycle.viewModelScope
import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.MadrugadaViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainState(
    val counter: Int
): MadrugadaState

class MainViewModel : MadrugadaViewModel<MainState>(MainState(0)) {

    val counter: StateFlow<String> = state.map { it.counter.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.value.counter.toString())

    fun increment() = viewModelScope.launch {
        state.emit(state.value.copy(counter = state.value.counter + 1))
    }

    fun reDeliver() {
        state.tryEmit(state.value)
    }
}