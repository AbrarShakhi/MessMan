package com.abrarshakhi.messman.core.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abrarshakhi.messman.core.ui.navigation.AppNavGraph
import com.abrarshakhi.messman.core.ui.navigation.AppRouteKey
import com.abrarshakhi.messman.core.ui.theme.MessManTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(navGraph: AppNavGraph) {
    val viewModel: AppViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    MessManTheme(
        theme = uiState.theme
    ) {
        AppShell(
            startRoute = AppRouteKey.HOME,
            navGraph = navGraph,
            viewModel = viewModel,
        )
    }
}

