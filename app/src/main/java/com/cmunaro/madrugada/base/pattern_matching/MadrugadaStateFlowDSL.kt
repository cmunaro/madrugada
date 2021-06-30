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

    class Builder<S : MadrugadaState> {
        private lateinit var viewModelScope: CoroutineScope
        private lateinit var initializer: MadrugadaStateFlowDSL<S>.() -> Unit
        private lateinit var state: MadrugadaStateFlow<S>

        fun withViewModelScope(viewModelScope: CoroutineScope): Builder<S> {
            this.viewModelScope = viewModelScope
            return this
        }

        fun withInitializer(initializer: MadrugadaStateFlowDSL<S>.() -> Unit): Builder<S> {
            this.initializer = initializer
            return this
        }

        fun withState(state: MadrugadaStateFlow<S>): Builder<S> {
            this.state = state
            return this
        }

        fun build() {
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