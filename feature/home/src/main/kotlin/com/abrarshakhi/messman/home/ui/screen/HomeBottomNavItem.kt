package com.abrarshakhi.messman.home.ui.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.abrarshakhi.messman.core.ui.navigation.AppRouteKey
import com.abrarshakhi.messman.core.ui.navigation.BarItemData
import com.abrarshakhi.messman.home.R

val homeBottomNavItem = BarItemData(
    key = AppRouteKey.HOME,
    icon = { Icon(painterResource(R.drawable.ic_home), contentDescription = "Home icon") },
    modifier = Modifier,
    enabled = true,
    label = { Text("Home") },
    alwaysShowLabel = false
)
