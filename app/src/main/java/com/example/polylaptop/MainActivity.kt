package com.example.polylaptop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bottomnavigation.BottomNavItem
import bottomnavigation.BottomNavigationBar
import bottomnavigation.ScreenBottomNavigation.CartScreen
import bottomnavigation.ScreenBottomNavigation.HomeScreen
import bottomnavigation.ScreenBottomNavigation.OrderScreen
import bottomnavigation.ScreenBottomNavigation.ProductDetail
<<<<<<< HEAD
import bottomnavigation.ScreenBottomNavigation.SearchScreen

=======
import bottomnavigation.ScreenBottomNavigation.Setting.DoiMatKhau
import bottomnavigation.ScreenBottomNavigation.Setting.LocationScreen
import bottomnavigation.ScreenBottomNavigation.Setting.QuenMatKhauScreen
>>>>>>> eba0876dece6d080d289b6006a0ebc6add4629c4
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan
import bottomnavigation.ScreenBottomNavigation.SettingScreen
import com.example.polylaptop.ui.theme.PolyLapTopTheme
import model.Screen
import view.AuthScreen
import view.OrderDetailsScreen
import view.WelcomeScreen
<<<<<<< HEAD
import java.net.URLDecoder
=======
import viewmodel.LocationViewModel
>>>>>>> eba0876dece6d080d289b6006a0ebc6add4629c4

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }

    }
}


@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        // Màn hình chào mừng
        composable(Screen.Welcome.route) {
            WelcomeScreen(onContinue = {
                navController.navigate(Screen.BottomNav.route)
            })
        }
        composable(
            route = Screen.ProductDetail.route + "/{chiTietSanPhamJson}",
            arguments = listOf(
                navArgument("chiTietSanPhamJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chiTietSanPhamJson = backStackEntry.arguments?.getString("chiTietSanPhamJson")

            // Truyền tham số chiTietSanPhamJson vào ProductDetail
            ProductDetail(
                navController = navController,
                chiTietSanPhamJson = chiTietSanPhamJson
            )
        }

        composable(
            route = Screen.OrderDetailsScreen.route + "/{chiTietSanPhamJson}",
            arguments = listOf(navArgument("chiTietSanPhamJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("chiTietSanPhamJson")
            val decodedJson = encodedJson?.let { URLDecoder.decode(it, "UTF-8") } // Giải mã chuỗi JSON
            OrderDetailsScreen(
                navController = navController,
                chiTietSanPhamJson = decodedJson
            )
        }

<<<<<<< HEAD


=======
        composable(Screen.QuenMatKhauScreen.route) {
            QuenMatKhauScreen(navController)
        }
>>>>>>> eba0876dece6d080d289b6006a0ebc6add4629c4

        composable(Screen.ThongTinCaNhan.route) {
            ThongTinCaNhan(navController)
        }
<<<<<<< HEAD
=======
        composable(Screen.LocationScreen.route){
            LocationScreen(viewModel = LocationViewModel())
        }

>>>>>>> eba0876dece6d080d289b6006a0ebc6add4629c4

        // Điều hướng Bottom Navigation với Scaffold và navController mới
        composable(Screen.BottomNav.route) {
            val bottomNavController = rememberNavController() // NavController cho BottomNavigation

            Scaffold(
                bottomBar = { BottomNavigationBar(bottomNavController) }
            ) { innerPadding ->
                NavHost(
                    navController = bottomNavController,
                    startDestination = BottomNavItem.Home.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(BottomNavItem.Home.route) {
                        HomeScreen(
                            bottomNavController = bottomNavController,
                            mainNavController = navController
                        )
                    }
                    composable(BottomNavItem.Cart.route) { CartScreen(bottomNavController,
                        mainNavController = navController) }
                    composable(BottomNavItem.Order.route) { OrderScreen(bottomNavController) }
                    composable(BottomNavItem.Setting.route) {
                        SettingScreen(
                            bottomNavController = bottomNavController,
                            mainNavController = navController
                        )
                    }
                }
            }
        }


        composable(
            Screen.SearchScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(500)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up,tween(500)) },
            exitTransition = {
                fadeOut(animationSpec = tween(500)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down,tween(500)) },
        ) {
            SearchScreen(navController)
        }

        // Màn hình Đăng nhập/Đăng ký
        composable(Screen.Auth.route) {
            AuthScreen(navController)
        }
    }
}

