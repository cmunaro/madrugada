package com.cmunaro.madrugada.base.pattern_matching

import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.pattern_matching.full.PatternMatchFullStateChange
import com.cmunaro.madrugada.base.pattern_matching.full.PatternMatchFullStateChangeInterface
import com.cmunaro.madrugada.base.pattern_matching.partial.PatternMatchPartialStateChange
import com.cmunaro.madrugada.base.pattern_matching.partial.PatternMatchPartialStateChangeInterface
import kotlin.reflect.KProperty1

@DslMarker
annotation class MadrugadaDSL

data class Matcher<S>(
    val properties: List<KProperty1<S, Any?>>,
    val action: Action,
    val partialState: Boolean
)

@MadrugadaDSL
abstract class MadrugadaStateFlowDSL<S : MadrugadaState>(
    override val matchersPartialState: ArrayList<Matcher<S>>
) :
    BaseMadrugadaStateFlow<S>,
    PatternMatchPartialStateChangeInterface<S> by PatternMatchPartialStateChange(
        matchersPartialState
    ),
    PatternMatchFullStateChangeInterface<S> by PatternMatchFullStateChange(matchersPartialState)

class MadrugadaStateFlowDSLImpl<S : MadrugadaState> : MadrugadaStateFlowDSL<S>(ArrayList())