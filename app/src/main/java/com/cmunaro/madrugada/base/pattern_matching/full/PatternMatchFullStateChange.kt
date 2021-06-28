package com.cmunaro.madrugada.base.pattern_matching.full

import com.cmunaro.madrugada.base.MadrugadaState
import com.cmunaro.madrugada.base.pattern_matching.Action1
import com.cmunaro.madrugada.base.pattern_matching.Matcher
import kotlin.reflect.KProperty1

class PatternMatchFullStateChange<S : MadrugadaState>(
    override val matchers: ArrayList<Matcher<S>>
) : PatternMatchFullStateChangeInterface<S> {
    override fun patternMatchFullStateChange(
        vararg properties: KProperty1<S, *>,
        action: (S) -> Unit
    ) {
        matchers.add(
            Matcher(
                properties = properties.toList(),
                action = Action1(action),
                partialState = false
            )
        )
    }
}