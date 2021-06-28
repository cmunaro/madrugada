package com.cmunaro.madrugada.base.pattern_matching.partial.comsuming

import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.pattern_matching.BaseMadrugadaStateFlow
import kotlin.reflect.KProperty1

interface PatternMatchPartialStateConsumingInterface<S : MadrugadaState> : BaseMadrugadaStateFlow<S> {
    fun <T> patternMatchPartialStateConsuming(property: KProperty1<S, T>, action: (T) -> Unit)

    fun <T1, T2> patternMatchPartialStateConsuming(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        action: (T1, T2) -> Unit
    )

    fun <T1, T2, T3> patternMatchPartialStateConsuming(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        property3: KProperty1<S, T3>,
        action: (T1, T2, T3) -> Unit
    )
}