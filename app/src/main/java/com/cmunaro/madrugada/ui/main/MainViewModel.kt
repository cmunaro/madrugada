package com.cmunaro.madrugada.ui.main

import androidx.lifecycle.viewModelScope
import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.MadrugadaViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    fun increment() = setState { copy(counter = counter + 1) }

    fun randomizeString() = setState { copy(string = UUID.randomUUID().toString()) }

    fun randomizeAll() = setState { MainState(Random.nextInt(), UUID.randomUUID().toString()) }

    fun reDeliver() = setState { copy() }

    fun fireEvent() = setState { state.value.copy(event = Unit) }
}