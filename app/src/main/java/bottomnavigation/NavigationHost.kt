package bottomnavigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bottomnavigation.ScreenBottomNavigation.CartScreen
import bottomnavigation.ScreenBottomNavigation.HomeScreen
import bottomnavigation.ScreenBottomNavigation.OrderScreen
import bottomnavigation.ScreenBottomNavigation.ProfileScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) { /* Home Screen UI */
            HomeScreen()
        }
        composable(BottomNavItem.Profile.route) { /* Search Screen UI */
            ProfileScreen()
        }
        composable(BottomNavItem.Cart.route) { /* Profile Screen UI */
            CartScreen()
        }
        composable(BottomNavItem.Order.route) { /* Profile Screen UI */
            OrderScreen()
        }
    }
}