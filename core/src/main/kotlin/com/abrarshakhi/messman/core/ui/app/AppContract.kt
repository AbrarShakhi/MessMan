package com.abrarshakhi.messman.core.ui.app

import com.abrarshakhi.messman.core.domain.model.AppTheme

data class AppUiState(
    val theme: AppTheme = AppTheme.SYSTEM
)


sealed interface AppAction