package com.cmunaro.madrugada.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlow
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlowDSL
import com.cmunaro.madrugada.base.pattern_matching.MadrugadaStateFlowDSLImpl
import com.cmunaro.madrugada.base.pattern_matching.Matcher
import kotlinx.coroutines.launch

open class MadrugadaViewModel<S : MadrugadaState>(initialState: S) : ViewModel() {
    operator fun invoke(
        initializer: MadrugadaStateFlowDSL<S>.() -> Unit
    ) {
        MadrugadaStateFlowDSLImpl<S>()
            .apply(initializer)
            .matchersPartialState.forEach { matcher: Matcher<S> ->
                viewModelScope.launch {
                    state.collectOnChangesOf(matcher)
                }
            }
    }

    open val state: MadrugadaStateFlow<S> = MadrugadaStateFlow.init(initialState)
}