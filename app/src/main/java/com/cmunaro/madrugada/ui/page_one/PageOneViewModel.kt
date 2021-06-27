package com.cmunaro.madrugada.ui.page_one

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PageOneViewModel: ViewModel() {
    val counter = MutableLiveData<Int>()

    fun increment() {
        counter.value = (counter.value?: 0) + 1
    }
}