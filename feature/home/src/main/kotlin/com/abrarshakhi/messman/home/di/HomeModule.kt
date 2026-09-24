package com.abrarshakhi.messman.home.di

import com.abrarshakhi.messman.home.ui.screen.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel<HomeViewModel> { HomeViewModel() }
}