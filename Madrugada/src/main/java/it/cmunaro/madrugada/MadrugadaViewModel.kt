package it.cmunaro.madrugada

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.cmunaro.madrugada.pattern_matching.MadrugadaStateFlow
import it.cmunaro.madrugada.pattern_matching.MadrugadaStateFlowDSL
import it.cmunaro.madrugada.pattern_matching.MadrugadaStateFlowDSLImpl
import it.cmunaro.madrugada.pattern_matching.MutableMadrugadaStateFlow

open class MadrugadaViewModel<S : MadrugadaState>(initialState: S) : ViewModel() {
    private val _state: MutableMadrugadaStateFlow<S> = MutableMadrugadaStateFlow.init(initialState)
    val state: MadrugadaStateFlow<S> = _state

    operator fun invoke(
        simulateMailBox: Boolean = false,
        initializer: MadrugadaStateFlowDSL<S>.() -> Unit
    ) {
        MadrugadaStateFlowDSLImpl.Builder<S>()
            .withViewModelScope(viewModelScope)
            .withInitializer(initializer)
            .withState(_state)
            .withSimulatedMailBox(simulateMailBox)
            .build()
    }

    fun setState(reducer: S.() -> S) {
        _state.tryEmit(
            _state.value.reducer()
        )
    }
}