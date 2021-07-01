package com.cmunaro.madrugada.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlow
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlowDSL
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlowDSLImpl
import com.cmunaro.madrugada.base.pattern_matching.MutableMadrugadaStateFlow

open class MadrugadaViewModel<S : MadrugadaState>(initialState: S) : ViewModel() {
    private val _state: MutableMadrugadaStateFlow<S> = MutableMadrugadaStateFlow.init(initialState)
    val state: MadrugadaStateFlow<S> = _state

    operator fun invoke(
        initializer: MadrugadaStateFlowDSL<S>.() -> Unit
    ) {
        MadrugadaStateFlowDSLImpl.Builder<S>()
            .withViewModelScope(viewModelScope)
            .withInitializer(initializer)
            .withState(_state)
            .build()
    }

    fun setState(reducer: S.() -> S) {
        _state.tryEmit(
            _state.value.reducer()
        )
    }
}