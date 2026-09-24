package com.abrarshakhi.messman.core.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier

sealed interface BottomBarKey : AppRouteKey

data class BarItemData(
    val key: BottomBarKey,
    val icon: @Composable () -> Unit,
    val modifier: Modifier = Modifier,
    val enabled: Boolean = true,
    val label: @Composable (() -> Unit)? = null,
    val alwaysShowLabel: Boolean = true,
)

@Composable
fun BottomBarKey.BottomBar(items: List<BarItemData>, backStack: SnapshotStateList<AppRouteKey>) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = this@BottomBar == item.key,
                onClick = { backStack.switchTabTo(item.key) },
                icon = item.icon,
                modifier = item.modifier,
                enabled = item.enabled,
                label = item.label,
                alwaysShowLabel = item.alwaysShowLabel,
            )
        }
    }
}
