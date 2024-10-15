package bottomnavigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bottomnavigation.ScreenBottomNavigation.CartScreen
import bottomnavigation.ScreenBottomNavigation.HomeScreen
import bottomnavigation.ScreenBottomNavigation.OrderScreen
import bottomnavigation.ScreenBottomNavigation.Setting.DoiMatKhau
import bottomnavigation.ScreenBottomNavigation.Setting.DoiMatKhau1
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan1
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan2
import bottomnavigation.ScreenBottomNavigation.SettingScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Home.route) { /* Home Screen UI */
            HomeScreen()
        }
        composable(BottomNavItem.Setting.route) { /* Search Screen UI */
            SettingScreen(navController )
        }
        composable(BottomNavItem.Cart.route) { /* Profile Screen UI */
            CartScreen()
        }
        composable(BottomNavItem.Order.route) { /* Profile Screen UI */
            OrderScreen(navController)
        }
        composable("thongTinCaNhan") {
            ThongTinCaNhan(navController)
        }
        composable("doiMatKhau") {
            DoiMatKhau(navController) // Màn hình Đổi mật khẩu (nếu cần)
        }
        composable("doiMatKhau1") {
            DoiMatKhau1(navController) // Màn hình Đổi mật khẩu (nếu cần)
        }
        composable("thongTinCaNhan1"){
            ThongTinCaNhan1(navController)
        }
        composable("thongTinCaNhan2"){
            ThongTinCaNhan2(navController)
        }
    }
}