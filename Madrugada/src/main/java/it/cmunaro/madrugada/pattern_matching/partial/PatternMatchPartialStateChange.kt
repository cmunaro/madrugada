package it.cmunaro.madrugada.pattern_matching.partial

import it.cmunaro.madrugada.MadrugadaState
import it.cmunaro.madrugada.pattern_matching.Action1
import it.cmunaro.madrugada.pattern_matching.Action2
import it.cmunaro.madrugada.pattern_matching.Action3
import it.cmunaro.madrugada.pattern_matching.Matcher
import kotlin.reflect.KProperty1


class PatternMatchPartialStateChange<S : MadrugadaState>(
    override val matchers: ArrayList<Matcher<S>>
) : PatternMatchPartialStateChangeInterface<S> {
    override fun <T> matchPartial(
        property: KProperty1<S, T>,
        action: (T) -> Unit
    ) {
        matchers.add(
            Matcher(
                properties = listOf(property),
                action = Action1(action),
                partialState = true
            )
        )
    }

    override fun <T1, T2> matchPartial(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        action: (T1, T2) -> Unit
    ) {
        matchers.add(
            Matcher(
                properties = listOf(property1, property2),
                action = Action2(action),
                partialState = true
            )
        )
    }

    override fun <T1, T2, T3> matchPartial(
        property1: KProperty1<S, T1>,
        property2: KProperty1<S, T2>,
        property3: KProperty1<S, T3>,
        action: (T1, T2, T3) -> Unit
    ) {
        matchers.add(
            Matcher(
                properties = listOf(property1, property2, property3),
                action = Action3(action),
                partialState = true
            )
        )
    }
}
