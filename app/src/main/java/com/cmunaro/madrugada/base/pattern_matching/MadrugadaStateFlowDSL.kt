package com.cmunaro.madrugada.base.pattern_matching

import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.pattern_matching.full.PatternMatchFullStateChange
import com.cmunaro.madrugada.base.pattern_matching.full.PatternMatchFullStateChangeInterface
import com.cmunaro.madrugada.base.pattern_matching.partial.PatternMatchPartialStateChange
import com.cmunaro.madrugada.base.pattern_matching.partial.PatternMatchPartialStateChangeInterface
import com.cmunaro.madrugada.base.pattern_matching.partial.comsuming.PatternMatchPartialStateConsuming
import com.cmunaro.madrugada.base.pattern_matching.partial.comsuming.PatternMatchPartialStateConsumingInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

@DslMarker
annotation class MadrugadaDSL

data class Matcher<S>(
    val properties: List<KProperty1<S, Any?>>,
    val action: Action,
    val partialState: Boolean,
    val nullifyAfterDeliver: Boolean = false
)

@MadrugadaDSL
abstract class MadrugadaStateFlowDSL<S : MadrugadaState>(
    override val matchers: ArrayList<Matcher<S>>
) :
    BaseMadrugadaStateFlow<S>,
    PatternMatchPartialStateChangeInterface<S> by PatternMatchPartialStateChange(matchers),
    PatternMatchFullStateChangeInterface<S> by PatternMatchFullStateChange(matchers),
    PatternMatchPartialStateConsumingInterface<S> by PatternMatchPartialStateConsuming(matchers)

class MadrugadaStateFlowDSLImpl<S : MadrugadaState> private constructor() :
    MadrugadaStateFlowDSL<S>(ArrayList()) {
    companion object {
        fun <S : MadrugadaState> get(
            viewModelScope: CoroutineScope,
            initializer: MadrugadaStateFlowDSL<S>.() -> Unit,
            state: MadrugadaStateFlow<S>
        ) {
            MadrugadaStateFlowDSLImpl<S>()
                .apply(initializer)
                .matchers.forEach { matcher: Matcher<S> ->
                    viewModelScope.launch {
                        state.runMatcherOnState(matcher)
                    }
                }
        }
    }
}