package com.abrarshakhi.messman.core.ui.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.abrarshakhi.messman.core.ui.navigation.AppRouteKey
import com.abrarshakhi.messman.core.ui.navigation.BottomBarKey

data class ChromeScope @OptIn(ExperimentalMaterial3Api::class) constructor(
    val backStack: SnapshotStateList<AppRouteKey>,
    val currentRoute: AppRouteKey?,
    val scrollBehavior: TopAppBarScrollBehavior,
)

class ScreenChrome(
    val topBar: @Composable (ChromeScope) -> Unit = {},
    val fab: @Composable (ChromeScope) -> Unit = {},
    val immersive: Boolean = false,
    val bottomBar: BottomBarKey? = null,
)