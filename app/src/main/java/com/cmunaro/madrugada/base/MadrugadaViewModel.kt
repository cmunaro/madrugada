package com.cmunaro.madrugada.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class MadrugadaViewModel<S : MadrugadaState>(initialState: S) : ViewModel() {
    operator fun invoke(
        initializer: MadrugadaStateFlowDSL<S>.() -> Unit
    ) {
        MadrugadaStateFlowDSLImpl<S>()
            .apply(initializer)
            .observers.forEach { (properties, action) ->
                viewModelScope.launch {
                    state.collectOnChangesOf(properties = properties, action)
                }
            }
    }

    open val state: MadrugadaStateFlow<S> = MadrugadaStateFlow.init(initialState)
}