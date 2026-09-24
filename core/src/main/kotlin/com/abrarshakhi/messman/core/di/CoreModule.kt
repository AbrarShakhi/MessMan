package com.abrarshakhi.messman.core.di

import com.abrarshakhi.messman.core.ui.app.AppViewModel
import com.abrarshakhi.messman.core.ui.snackbar.SnackbarDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val coreModule = module {
    viewModelOf(::AppViewModel)
    singleOf(::SnackbarDispatcher)
}
