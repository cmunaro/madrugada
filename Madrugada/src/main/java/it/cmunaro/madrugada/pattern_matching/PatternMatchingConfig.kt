package it.cmunaro.madrugada.pattern_matching

sealed interface PatternMatchingConfig {
    object Sequential: PatternMatchingConfig
    object Parallel: PatternMatchingConfig
}