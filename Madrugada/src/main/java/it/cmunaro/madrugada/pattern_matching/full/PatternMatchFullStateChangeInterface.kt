package it.cmunaro.madrugada.pattern_matching.full

import it.cmunaro.madrugada.MadrugadaState
import it.cmunaro.madrugada.pattern_matching.BaseMadrugadaStateFlow
import kotlin.reflect.KProperty1

interface PatternMatchFullStateChangeInterface<S : MadrugadaState> : BaseMadrugadaStateFlow<S> {
    fun patternMatchFull(vararg properties: KProperty1<S, *>, action: (S) -> Unit)
}
