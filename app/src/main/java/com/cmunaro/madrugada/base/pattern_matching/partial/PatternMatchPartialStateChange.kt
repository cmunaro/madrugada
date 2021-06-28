package com.cmunaro.madrugada.base.pattern_matching.partial

import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.pattern_matching.Action1
import com.cmunaro.madrugada.base.pattern_matching.Action2
import com.cmunaro.madrugada.base.pattern_matching.Action3
import com.cmunaro.madrugada.base.pattern_matching.Matcher
import kotlin.reflect.KProperty1


class PatternMatchPartialStateChange<S : MadrugadaState>(override val matchersPartialState: ArrayList<Matcher<S>>) :
    PatternMatchPartialStateChangeInterface<S> {
    override fun <T> patternMatchPartialStateChange(
        property: KProperty1<S, T>,
        action: (T) -> Unit
    ) {
        matchersPartialState.add(
            Matcher(
                properties = listOf(property),
                action = Action1(action),
                partialState = true
            )
        )
    }

    override fun <T1, T2> patternMatchPartialStateChange(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        action: (T1, T2) -> Unit
    ) {
        matchersPartialState.add(
            Matcher(
                properties = listOf(property1, property2),
                action = Action2(action),
                partialState = true
            )
        )
    }

    override fun <T1, T2, T3> patternMatchPartialStateChange(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        property3: KProperty1<S, T3>,
        action: (T1, T2, T3) -> Unit
    ) {
        matchersPartialState.add(
            Matcher(
                properties = listOf(property1, property2, property3),
                action = Action3(action),
                partialState = true
            )
        )
    }
}
