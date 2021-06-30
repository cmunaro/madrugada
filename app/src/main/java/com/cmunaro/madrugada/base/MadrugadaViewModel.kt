package com.cmunaro.madrugada.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlow
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlowDSL
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlowDSLImpl

open class MadrugadaViewModel<S : MadrugadaState>(initialState: S) : ViewModel() {
    open val state: MadrugadaStateFlow<S> = MadrugadaStateFlow.init(initialState)

    operator fun invoke(
        initializer: MadrugadaStateFlowDSL<S>.() -> Unit
    ) {
        MadrugadaStateFlowDSLImpl.Builder<S>()
            .withViewModelScope(viewModelScope)
            .withInitializer(initializer)
            .withState(state)
            .build()
    }
}