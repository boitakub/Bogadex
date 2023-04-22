package fr.boitakub.bogadex.preferences

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import fr.boitakub.bogadex.preferences.user.UserSettingsScreen

object PreferencesNavigation {

    const val ROUTE: String = "preferences"

    fun navigateTo(): String {
        return ROUTE
    }

    @Composable
    fun onNavigation(navController: NavHostController, navBackStackEntry: NavBackStackEntry) {
        UserSettingsScreen(navController = navController)
    }
}
