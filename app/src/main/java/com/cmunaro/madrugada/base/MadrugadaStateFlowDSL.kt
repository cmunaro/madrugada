package com.cmunaro.madrugada.base

import kotlin.reflect.KProperty1

@DslMarker
annotation class MadrugadaDSL

data class Matcher<S>(
    val properties: List<KProperty1<S, Any?>>,
    val action: Action,
    val partialState: Boolean
)

@MadrugadaDSL
abstract class MadrugadaStateFlowDSL<S : MadrugadaState> {
    abstract val matchersPartialState: ArrayList<Matcher<S>>

    fun <T> patternMatchPartialStateChange(property: KProperty1<S, T>, action: (T) -> Unit) {
        matchersPartialState.add(
            Matcher(
                properties = listOf(property),
                action = Action1(action),
                partialState = true
            )
        )
    }

    fun <T1, T2> patternMatchPartialStateChange(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        action: (T1, T2) -> Unit
    ) {
        matchersPartialState.add(
            Matcher(
                properties = listOf(property1, property2),
                action = Action2(action),
                partialState = true
            )
        )
    }

    fun <T1, T2, T3> patternMatchPartialStateChange(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        property3: KProperty1<S, T3>,
        action: (T1, T2, T3) -> Unit
    ) {
        matchersPartialState.add(
            Matcher(
                properties = listOf(property1, property2, property3),
                action = Action3(action),
                partialState = true
            )
        )
    }

    fun patternMatchStateChange(vararg properties: KProperty1<S, *>, action: (S) -> Unit) {
        matchersPartialState.add(
            Matcher(
                properties = properties.toList(),
                action = Action1(action),
                partialState = false
            )
        )
    }
}

class MadrugadaStateFlowDSLImpl<S : MadrugadaState> : MadrugadaStateFlowDSL<S>() {
    override val matchersPartialState = ArrayList<Matcher<S>>()
}