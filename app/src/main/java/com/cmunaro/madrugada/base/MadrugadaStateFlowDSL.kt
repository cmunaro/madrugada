package com.cmunaro.madrugada.base

import kotlin.reflect.KProperty1

@DslMarker
annotation class MadrugadaDSL

@MadrugadaDSL
abstract class MadrugadaStateFlowDSL<S : MadrugadaState> {
    abstract val observers: HashMap<Array<out KProperty1<S, *>>, (S) -> Unit>

    fun patternMatchPartialStateChange(vararg properties: KProperty1<S, *>, action: (S) -> Unit) {
        observers[properties] = action
    }
}

class MadrugadaStateFlowDSLImpl<S: MadrugadaState>: MadrugadaStateFlowDSL<S>() {
    override val observers = HashMap<Array<out KProperty1<S, *>>, (S) -> Unit>()
}