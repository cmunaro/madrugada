package com.cmunaro.madrugada.base.pattern_matching

import com.cmunaro.madrugada.base.MadrugadaState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions

interface BaseMadrugadaStateFlow<S : MadrugadaState> {
    val matchers: ArrayList<Matcher<S>>
}

interface MadrugadaStateFlow<S: MadrugadaState>: StateFlow<S>

@OptIn(InternalCoroutinesApi::class)
class MutableMadrugadaStateFlow<S : MadrugadaState> private constructor(
    private val initialState: S,
    private val stateFlow: MutableStateFlow<S> = MutableStateFlow(initialState)
) : MutableStateFlow<S> by stateFlow, MadrugadaStateFlow<S> {
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

    suspend fun runMatcherOnState(matcher: Matcher<S>) {
        stateFlow.collect { newState: S ->
            val hasChanged = matcher.properties.all { property ->
                property.get(lastState) != property.get(newState)
            }
            if (hasChanged) {
                matcher.action.invoke(
                    if (!matcher.partialState) listOf(newState)
                    else matcher.properties
                        .map { property -> property.get(newState) }
                )
                if (matcher.nullifyAfterDeliver) {
                    val copy = newState::class.memberFunctions.first { it.name == "copy" }
                    val instanceParam = copy.instanceParameter!!
                    val nullifyParameters = copy.parameters.filter { copyParameter ->
                        matcher.properties.any { copyParameter.name == it.name }
                    }.map { it to null }
                    val copyParameter = mapOf(instanceParam to newState) + nullifyParameters
                    val result = copy.callBy(copyParameter)
                    stateFlow.emit(result as S)
                }
            }
        }
    }

    companion object {
        fun <S : MadrugadaState> init(initialState: S) = MutableMadrugadaStateFlow(initialState)
    }
}