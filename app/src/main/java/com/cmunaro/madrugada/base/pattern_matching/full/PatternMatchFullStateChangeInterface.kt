package com.cmunaro.madrugada.base.pattern_matching.full

import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.pattern_matching.BaseMadrugadaStateFlow
import kotlin.reflect.KProperty1

interface PatternMatchFullStateChangeInterface<S : MadrugadaState> : BaseMadrugadaStateFlow<S> {
    fun patternMatchFullStateChange(vararg properties: KProperty1<S, *>, action: (S) -> Unit)
}
