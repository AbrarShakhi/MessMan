package com.abrarshakhi.messman.core.ui.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import com.abrarshakhi.messman.core.ui.app.ScreenChrome

class AppNavGraph(
    val entries: EntryProviderScope<AppRouteKey>.(backStack: SnapshotStateList<AppRouteKey>) -> Unit,
    val chrome: (AppRouteKey) -> ScreenChrome,
    val bottomBarItems: List<BarItemData>,
)
