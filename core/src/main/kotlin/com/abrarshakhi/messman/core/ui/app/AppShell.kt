package com.abrarshakhi.messman.core.ui.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.abrarshakhi.messman.core.ui.navigation.AppNavigation
import com.abrarshakhi.messman.core.ui.navigation.AppRouteKey
import com.abrarshakhi.messman.core.ui.navigation.BottomBar
import com.abrarshakhi.messman.core.ui.navigation.currentRoute
import com.abrarshakhi.messman.core.ui.navigation.rememberAppBackStack
import com.abrarshakhi.messman.core.ui.snackbar.SnackbarDispatcher
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(startRoute: AppRouteKey, viewModel: AppViewModel) {
    val backStack = rememberAppBackStack(startRoute)
    val current = backStack.currentRoute()
    val chrome = current?.chrome()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val chromeScope = ChromeScope(backStack, current, scrollBehavior)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarDispatcher: SnackbarDispatcher = koinInject()

    LaunchedEffect(current) {
        scrollBehavior.state.contentOffset = 0f
        scrollBehavior.state.heightOffset = 0f
    }


    LaunchedEffect(snackbarDispatcher) {
        snackbarDispatcher.messages.collect { message ->
            snackbarHostState.showSnackbar(
                message = message.text,
                withDismissAction = message.duration != SnackbarDuration.Short,
                duration = message.duration,
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { chrome?.topBar?.invoke(chromeScope) },
        bottomBar = {
            chrome?.bottomBar?.let {
                BottomBar(it, backStack)
            }
        },
        floatingActionButton = { chrome?.fab?.invoke(chromeScope) },
    ) { innerPadding ->
        AppNavigation(
            backStack = backStack, modifier = Modifier.padding(innerPadding)
        )
    }
}