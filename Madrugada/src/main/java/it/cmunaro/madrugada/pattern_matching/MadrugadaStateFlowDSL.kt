package it.cmunaro.madrugada.pattern_matching

import it.cmunaro.madrugada.MadrugadaState
import it.cmunaro.madrugada.pattern_matching.PatternMatchingConfig.Parallel
import it.cmunaro.madrugada.pattern_matching.PatternMatchingConfig.Sequential
import it.cmunaro.madrugada.pattern_matching.full.PatternMatchFullStateChange
import it.cmunaro.madrugada.pattern_matching.full.PatternMatchFullStateChangeInterface
import it.cmunaro.madrugada.pattern_matching.partial.PatternMatchPartialStateChange
import it.cmunaro.madrugada.pattern_matching.partial.PatternMatchPartialStateChangeInterface
import it.cmunaro.madrugada.pattern_matching.partial.comsuming.PatternMatchPartialStateConsuming
import it.cmunaro.madrugada.pattern_matching.partial.comsuming.PatternMatchPartialStateConsumingInterface
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
        private lateinit var state: MutableMadrugadaStateFlow<S>
        private lateinit var patternMatchingConfiguration: PatternMatchingConfig

        fun withViewModelScope(viewModelScope: CoroutineScope): Builder<S> {
            this.viewModelScope = viewModelScope
            return this
        }

        fun withInitializer(initializer: MadrugadaStateFlowDSL<S>.() -> Unit): Builder<S> {
            this.initializer = initializer
            return this
        }

        fun withState(state: MutableMadrugadaStateFlow<S>): Builder<S> {
            this.state = state
            return this
        }

        fun withPatternMatchingConfiguration(config: PatternMatchingConfig): Builder<S> {
            this.patternMatchingConfiguration = config
            return this
        }

        fun build() {
            val instance = MadrugadaStateFlowDSLImpl<S>()
                .apply(initializer)

            when (patternMatchingConfiguration) {
                Sequential -> runSequentialMatchers(instance)
                Parallel -> runParallelMatcher(instance)
            }
        }

        private fun runSequentialMatchers(instance: MadrugadaStateFlowDSLImpl<S>) =
            viewModelScope.launch {
                state.runMatchersOnState(instance.matchers)
            }

        private fun runParallelMatcher(instance: MadrugadaStateFlowDSLImpl<S>) =
            instance.matchers.forEach { matcher: Matcher<S> ->
                viewModelScope.launch {
                    state.runMatcherOnState(matcher)
                }
            }
    }
}