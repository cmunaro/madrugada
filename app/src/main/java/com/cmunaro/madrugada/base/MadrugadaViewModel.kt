package com.cmunaro.madrugada.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

open class MadrugadaViewModel<S : MadrugadaState>(initialState: S) : ViewModel() {
    fun observeState(
        owner: LifecycleOwner,
        initializer: MadrugadaStateFlowDSL<S>.() -> Unit
    ) {
        MadrugadaStateFlowDSLImpl<S>()
            .apply(initializer)
            .observers.forEach { (properties, action) ->
                owner.lifecycleScope.launch {
                    state.collectOnChangesOf(properties = properties, action)
                }
            }
    }

    open val state: MadrugadaStateFlow<S> = MadrugadaStateFlow.init(initialState)
}