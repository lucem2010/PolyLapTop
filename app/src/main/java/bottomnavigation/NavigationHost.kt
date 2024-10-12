package bottomnavigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) { /* Home Screen UI */ }
        composable(BottomNavItem.Search.route) { /* Search Screen UI */ }
        composable(BottomNavItem.Profile.route) { /* Profile Screen UI */ }
    }
}