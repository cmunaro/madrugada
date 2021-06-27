package com.cmunaro.madrugada.base

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlin.reflect.KProperty1

@OptIn(InternalCoroutinesApi::class)
class MadrugadaStateFlow<S : MadrugadaState> private constructor(
    private val initialState: S,
    private val stateFlow: MutableStateFlow<S> = MutableStateFlow(initialState)
) : MutableStateFlow<S> by stateFlow {
    private var lastState: S = initialState

    override var value: S
        get() = stateFlow.value
        set(value) {
            lastState = this.value
            stateFlow.value = value
        }

    override suspend fun emit(value: S) {
        lastState = this.value
        stateFlow.emit(value)
    }

    override fun tryEmit(value: S): Boolean {
        lastState = this.value
        return stateFlow.tryEmit(value)
    }

    @Suppress("unused")
    suspend inline fun collect(crossinline action: suspend (value: S) -> Unit): Unit =
        collect(object : FlowCollector<S> {
            override suspend fun emit(value: S) = action(value)
        })

    @Suppress("unused")
    suspend fun collectOnChangesOf(vararg properties: KProperty1<S, *>, action: (S) -> Unit) {
        stateFlow.collect { newState ->
            val hasChanged = properties.all { property ->
                property.get(lastState) != property.get(newState)
            }
            if (hasChanged) action(newState)
        }
    }

    companion object {
        fun <S : MadrugadaState> init(initialState: S) = MadrugadaStateFlow(initialState)
    }
}
