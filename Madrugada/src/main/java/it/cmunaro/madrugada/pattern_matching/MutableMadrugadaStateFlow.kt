package it.cmunaro.madrugada.pattern_matching

import it.cmunaro.madrugada.MadrugadaState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions

interface BaseMadrugadaStateFlow<S : MadrugadaState> {
    val matchers: ArrayList<Matcher<S>>
}

interface MadrugadaStateFlow<S : MadrugadaState> : StateFlow<S>

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
        stateFlow.collect { newState ->
            val matched = patternMatchChange(matcher, newState)
            if (matched) {
                deliverAction(matcher, newState)
                maybeNullifyStateByMatcher(newState, matcher)
            }
        }
    }

    suspend fun runMatchersOnState(matchers: java.util.ArrayList<Matcher<S>>) {
        stateFlow.collect { newState ->
            val matchedMatcher = matchers.firstOrNull { matcher ->
                patternMatchChange(matcher, newState)
            } ?: return@collect
            deliverAction(matchedMatcher, newState)
            maybeNullifyStateByMatcher(newState, matchedMatcher)
        }
    }

    private suspend fun maybeNullifyStateByMatcher(newState: S, matcher: Matcher<S>) {
        if (!matcher.nullifyAfterDeliver) return
        val copy = newState::class.memberFunctions.first { it.name == "copy" }
        val instanceParam = copy.instanceParameter!!
        val nullifyParameters = copy.parameters.filter { copyParameter ->
            matcher.properties.any { copyParameter.name == it.name }
        }.map { it to null }
        val copyParameter = mapOf(instanceParam to newState) + nullifyParameters
        val result = copy.callBy(copyParameter)
        @Suppress("unchecked_cast")
        stateFlow.emit(result as S)
    }

    private fun patternMatchChange(matcher: Matcher<S>, newState: S) =
        matcher.properties.all { property ->
            property.get(lastState) != property.get(newState)
        }

    private fun deliverAction(matchedMatcher: Matcher<S>, newState: S) {
        matchedMatcher.action.invoke(
            if (!matchedMatcher.partialState) listOf(newState)
            else matchedMatcher.properties
                .map { property -> property.get(newState) }
        )
    }

    companion object {
        fun <S : MadrugadaState> init(initialState: S) = MutableMadrugadaStateFlow(initialState)
    }
}