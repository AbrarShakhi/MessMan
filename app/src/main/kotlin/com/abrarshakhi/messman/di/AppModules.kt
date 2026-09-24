package com.abrarshakhi.messman.di

import com.abrarshakhi.messman.core.di.coreModule
import com.abrarshakhi.messman.home.di.homeModule
import org.koin.core.module.Module

val appModules: List<Module> = listOf(
    coreModule, homeModule
)
