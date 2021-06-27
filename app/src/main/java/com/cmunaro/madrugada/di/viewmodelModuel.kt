package com.cmunaro.madrugada.di

import com.cmunaro.madrugada.ui.main.MainViewModel
import com.cmunaro.madrugada.ui.page_one.PageOneViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { PageOneViewModel() }
}