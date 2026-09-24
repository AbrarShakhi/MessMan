package com.abrarshakhi.messman.core.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface BottomBarKey


@Serializable
sealed interface AppRouteKey : NavKey {

    @Serializable
    data object HOME : AppRouteKey, BottomBarKey
}


fun AppRouteKey.isInBottomBar() = this is BottomBarKey

/*
If a AppRouteKey is not BottomBarKey, screen will not be part of bottom Bar
 */