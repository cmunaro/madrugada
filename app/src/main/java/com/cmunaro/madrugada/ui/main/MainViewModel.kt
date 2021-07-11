package com.cmunaro.madrugada.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import it.cmunaro.madrugada.MadrugadaState
import it.cmunaro.madrugada.MadrugadaViewModel
import kotlinx.coroutines.flow.map
import java.util.*
import kotlin.random.Random

data class MainState(
    val counter: Int,
    val string: String,
    val event: Unit? = null
): MadrugadaState

class MainViewModel : MadrugadaViewModel<MainState>(MainState(0, "UUID")) {

    val counter: LiveData<String> = state.map { it.counter.toString() }.asLiveData()

    fun increment() = setState { copy(counter = counter + 1) }

    fun randomizeString() = setState { copy(string = UUID.randomUUID().toString()) }

    fun randomizeAll() = setState { MainState(Random.nextInt(), UUID.randomUUID().toString()) }

    fun reDeliver() = setState { copy() }

    fun fireEvent() = setState { state.value.copy(event = Unit) }
}