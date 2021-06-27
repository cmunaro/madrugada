package com.cmunaro.madrugada.ui.main

import androidx.lifecycle.viewModelScope
import com.cmunaro.madrugada.base.MadrugadaViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : MadrugadaViewModel() {
    private val _counter = MutableStateFlow(123)
    val counter: StateFlow<String> = _counter.map { it.toString() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, _counter.value.toString())

    fun increment() = viewModelScope.launch {
        _counter.emit(_counter.value + 1)
    }
}