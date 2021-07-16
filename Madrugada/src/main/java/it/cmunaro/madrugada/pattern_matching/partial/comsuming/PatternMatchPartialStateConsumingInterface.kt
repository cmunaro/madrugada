package it.cmunaro.madrugada.pattern_matching.partial.comsuming

import it.cmunaro.madrugada.MadrugadaState
import it.cmunaro.madrugada.pattern_matching.BaseMadrugadaStateFlow
import kotlin.reflect.KProperty1

interface PatternMatchPartialStateConsumingInterface<S : MadrugadaState> :
    BaseMadrugadaStateFlow<S> {
    fun <T> matchEvent(property: KProperty1<S, T>, action: (T) -> Unit)

    fun <T1, T2> matchEvent(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        action: (T1, T2) -> Unit
    )

    fun <T1, T2, T3> matchEvent(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        property3: KProperty1<S, T3>,
        action: (T1, T2, T3) -> Unit
    )
}