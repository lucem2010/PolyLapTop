package com.example.polylaptop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bottomnavigation.BottomNavItem
import bottomnavigation.BottomNavigationBar
import bottomnavigation.ScreenBottomNavigation.CartScreen
import bottomnavigation.ScreenBottomNavigation.HomeScreen
import bottomnavigation.ScreenBottomNavigation.OrderScreen
import bottomnavigation.ScreenBottomNavigation.ProductDetail
import bottomnavigation.ScreenBottomNavigation.Setting.DoiMatKhau
import bottomnavigation.ScreenBottomNavigation.Setting.DoiMatKhau1
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan1
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan2
import bottomnavigation.ScreenBottomNavigation.SettingScreen
import com.example.polylaptop.ui.theme.PolyLapTopTheme
import model.Screen
import view.AuthScreen
import view.WelcomeScreen

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
        composable(Screen.ProductDetail.route) {
            ProductDetail(navController = navController)
        }

        composable(Screen.DoiMatKhau.route) {
            DoiMatKhau(navController)
        }

        composable(Screen.DoiMatKhau1.route) {
            DoiMatKhau1(navController)
        }

        composable(Screen.ThongTinCaNhan.route) {
            ThongTinCaNhan(navController)
        }

        composable(Screen.ThongTinCaNhan1.route) {
            ThongTinCaNhan1(navController)
        }

        composable(Screen.ThongTinCaNhan2.route) {
            ThongTinCaNhan2(navController)
        }

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
                    composable(BottomNavItem.Cart.route) { CartScreen(bottomNavController) }
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

        // Màn hình Đăng nhập/Đăng ký
        composable(Screen.Auth.route) {
            AuthScreen(navController)
        }
    }
}

