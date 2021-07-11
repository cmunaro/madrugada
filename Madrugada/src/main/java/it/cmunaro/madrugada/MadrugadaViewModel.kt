package it.cmunaro.madrugada

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.cmunaro.madrugada.pattern_matching.*
import it.cmunaro.madrugada.pattern_matching.PatternMatchingConfig.Sequential

open class MadrugadaViewModel<S : MadrugadaState>(initialState: S) : ViewModel() {
    private val _state: MutableMadrugadaStateFlow<S> = MutableMadrugadaStateFlow.init(initialState)
    val state: MadrugadaStateFlow<S> = _state

    operator fun invoke(
        patternMatchingConfiguration: PatternMatchingConfig = Sequential,
        initializer: MadrugadaStateFlowDSL<S>.() -> Unit
    ) {
        MadrugadaStateFlowDSLImpl.Builder<S>()
            .withViewModelScope(viewModelScope)
            .withInitializer(initializer)
            .withState(_state)
            .withPatternMatchingConfiguration(patternMatchingConfiguration)
            .build()
    }

    fun setState(reducer: S.() -> S) {
        _state.tryEmit(
            _state.value.reducer()
        )
    }
}