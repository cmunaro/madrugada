package com.cmunaro.madrugada.ui.main

import androidx.lifecycle.viewModelScope
import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.MadrugadaViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

data class MainState(
    val counter: Int,
    val string: String,
    val event: Unit? = null
): MadrugadaState

class MainViewModel : MadrugadaViewModel<MainState>(MainState(0, "UUID")) {

    val counter: StateFlow<String> = state.map { it.counter.toString() }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.value.counter.toString())

    fun increment() = viewModelScope.launch {
        state.emit(state.value.copy(counter = state.value.counter + 1))
    }

    fun randomizeString() = viewModelScope.launch {
        state.emit(state.value.copy(string = UUID.randomUUID().toString()))
    }

    fun randomizeAll() = viewModelScope.launch {
        state.emit(MainState(Random.nextInt(), UUID.randomUUID().toString()))
    }

    fun reDeliver() {
        state.tryEmit(state.value)
    }

    fun fireEvent() = viewModelScope.launch {
        state.emit(state.value.copy(event = Unit))
    }
}