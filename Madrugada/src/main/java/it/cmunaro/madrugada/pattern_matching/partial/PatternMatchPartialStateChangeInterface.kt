package it.cmunaro.madrugada.pattern_matching.partial

import it.cmunaro.madrugada.MadrugadaState
import it.cmunaro.madrugada.pattern_matching.BaseMadrugadaStateFlow
import kotlin.reflect.KProperty1

interface PatternMatchPartialStateChangeInterface<S : MadrugadaState> : BaseMadrugadaStateFlow<S> {
    fun <T> matchPartial(property: KProperty1<S, T>, action: (T) -> Unit)

    fun <T1, T2> matchPartial(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        action: (T1, T2) -> Unit
    )

    fun <T1, T2, T3> matchPartial(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        property3: KProperty1<S, T3>,
        action: (T1, T2, T3) -> Unit
    )
}