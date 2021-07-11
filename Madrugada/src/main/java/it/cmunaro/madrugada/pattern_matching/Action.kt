package it.cmunaro.madrugada.pattern_matching

interface Action {
    fun invoke(changedValues: List<Any?>)
}

data class Action1<T1>(
    val action: (T1) -> Unit
) : Action {
    override fun invoke(changedValues: List<Any?>) =
        action.invoke(changedValues[0] as T1)
}

data class Action2<T1, T2>(
    val action: (T1, T2) -> Unit
) : Action {
    override fun invoke(changedValues: List<Any?>) = action.invoke(
        changedValues[0] as T1,
        changedValues[1] as T2,
    )
}

data class Action3<T1, T2, T3>(
    val action: (T1, T2, T3) -> Unit
) : Action {
    override fun invoke(changedValues: List<Any?>) = action.invoke(
        changedValues[0] as T1,
        changedValues[1] as T2,
        changedValues[2] as T3,
    )
}