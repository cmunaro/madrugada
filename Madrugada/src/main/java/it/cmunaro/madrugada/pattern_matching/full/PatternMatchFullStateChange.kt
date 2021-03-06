package it.cmunaro.madrugada.pattern_matching.full

import it.cmunaro.madrugada.MadrugadaState
import it.cmunaro.madrugada.pattern_matching.Action1
import it.cmunaro.madrugada.pattern_matching.Matcher
import kotlin.reflect.KProperty1

class PatternMatchFullStateChange<S : MadrugadaState>(
    override val matchers: ArrayList<Matcher<S>>
) : PatternMatchFullStateChangeInterface<S> {
    override fun patternMatchFull(
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