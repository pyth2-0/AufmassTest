package com.aufmass.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object AufmassDetail : Screen("aufmass/{aufmassId}") {
        fun createRoute(aufmassId: Long) = "aufmass/$aufmassId"
    }
}
