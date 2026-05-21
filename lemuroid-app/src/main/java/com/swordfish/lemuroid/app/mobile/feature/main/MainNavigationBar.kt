package com.swordfish.lemuroid.app.mobile.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.app.mobile.shared.compose.ui.AppDockBackground
import com.swordfish.lemuroid.app.mobile.shared.compose.ui.AppOnBackground
import com.swordfish.lemuroid.app.mobile.shared.compose.ui.AppPrimary
import com.swordfish.lemuroid.app.mobile.shared.compose.ui.AppTheme

private val Muted = Color(0xFF666666)
private val NavBg = AppDockBackground

private val NdotFontFamily = FontFamily(Font(R.font.ndot57_regular))

@Composable
fun MainNavigationBar(
    currentRoute: MainRoute?,
    navController: NavHostController,
) {
    MainNavigationBarContent(
        currentRoute = currentRoute,
        onNavigationItemClick = { destination ->
            navController.navigate(destination.route.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = false
                }
                launchSingleTop = true
                restoreState = false
            }
        }
    )
}

@Composable
private fun MainNavigationBarContent(
    currentRoute: MainRoute?,
    onNavigationItemClick: (MainNavigationRoutes) -> Unit,
) {
    AnimatedVisibility(
        visible = currentRoute?.showBottomNavigation != false,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        LemuroidNavigationBar(currentRoute, onNavigationItemClick)
    }
}

@Composable
private fun LemuroidNavigationBar(
    currentRoute: MainRoute?,
    onNavigationItemClick: (MainNavigationRoutes) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 24.dp) // Closer to edges
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Less rounded, more rectangular
        color = NavBg.copy(alpha = 0.95f),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            modifier = Modifier.height(80.dp).padding(top = 8.dp),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            MainNavigationRoutes.entries.forEach { destination ->
                val isSelected = currentRoute?.root == destination.route
                val iconDrawable = if (isSelected) destination.selectedIcon else destination.unselectedIcon

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = iconDrawable,
                            contentDescription = stringResource(destination.titleId),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.titleId),
                            fontFamily = NdotFontFamily
                        )
                    },
                    selected = isSelected,
                    alwaysShowLabel = true,
                    colors =
                        NavigationBarItemDefaults.colors(
                            indicatorColor = AppPrimary,
                            selectedIconColor = AppOnBackground,
                            selectedTextColor = AppOnBackground,
                            unselectedIconColor = Muted,
                            unselectedTextColor = Muted,
                        ),
                    onClick = {
                        onNavigationItemClick(destination)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun MainNavigationBarPreview() {
    AppTheme {
        MainNavigationBarContent(
            currentRoute = MainRoute.HOME,
            onNavigationItemClick = {}
        )
    }
}
