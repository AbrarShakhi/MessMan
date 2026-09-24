package com.abrarshakhi.messman.navigation

import com.abrarshakhi.messman.core.ui.navigation.AppNavGraph
import com.abrarshakhi.messman.core.ui.navigation.AppRouteKey
import com.abrarshakhi.messman.home.ui.HomeRoute
import com.abrarshakhi.messman.home.ui.screen.homeBottomNavItem
import com.abrarshakhi.messman.home.ui.screen.homeScreenChrome

val messManNavGraph = AppNavGraph(
    entries = {
        entry<AppRouteKey.HOME> { HomeRoute() }
    },
    chrome = { route ->
        when (route) {
            AppRouteKey.HOME -> homeScreenChrome()
        }
    },
    bottomBarItems = listOf(homeBottomNavItem),
)
