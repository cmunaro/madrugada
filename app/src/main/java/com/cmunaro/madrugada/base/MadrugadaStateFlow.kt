package com.cmunaro.madrugada.base

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

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
    suspend fun collectOnChangesOf(matcher: Matcher<S>) {
        stateFlow.collect { newState ->
            val hasChanged = matcher.properties.all { property ->
                property.get(lastState) != property.get(newState)
            }
            if (hasChanged) {
                matcher.action.invoke(
                    if (!matcher.partialState) listOf(newState)
                    else matcher.properties
                        .map { property -> property.get(newState) }
                )
            }
        }
    }

    companion object {
        fun <S : MadrugadaState> init(initialState: S) = MadrugadaStateFlow(initialState)
    }
}
