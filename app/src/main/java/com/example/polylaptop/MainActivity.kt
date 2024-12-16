package com.example.polylaptop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bottomnavigation.BottomNavItem
import bottomnavigation.BottomNavigationBar
import bottomnavigation.ScreenBottomNavigation.CartScreen
import bottomnavigation.ScreenBottomNavigation.DanhgiaScreenBasic
import bottomnavigation.ScreenBottomNavigation.HomeScreen
import bottomnavigation.ScreenBottomNavigation.Order.DeliveredOrdersScreen
import bottomnavigation.ScreenBottomNavigation.OrderScreen
import bottomnavigation.ScreenBottomNavigation.ProductDetail
import bottomnavigation.ScreenBottomNavigation.SearchScreen
import bottomnavigation.ScreenBottomNavigation.Setting.DoiMatKhau
import bottomnavigation.ScreenBottomNavigation.Setting.ThongTinCaNhan
import bottomnavigation.ScreenBottomNavigation.SettingScreen
import com.example.polylaptop.ui.theme.PolyLapTopTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import data.ApiService
import model.Screen
import view.AuthScreen
import view.ChatScreen
import view.OrderDetailsScreen
import view.WelcomeScreen
import viewmodel.ChatViewModel
import viewmodel.PaymentViewModel
import viewmodel.DonHangViewModel
import viewmodel.UserViewModel
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var chatViewModel: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Khởi tạo ViewModel
        paymentViewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        // khởi tạo zalopay
        ZaloPaySDK.init(553, Environment.SANDBOX)

        enableEdgeToEdge()
        setContent {

            val userViewModel: UserViewModel = viewModel()
            val donViewModel: DonHangViewModel = viewModel()
            userViewModel.loadTheme(applicationContext) // Load theme khi khởi tạo ứng dụng
            MyApp(
                userViewModel,
                paymentViewModel,
                chatViewModel,
                donViewModel
            ) // Truyền ViewModel vào MyApp
        }

    }

    // chuyển màn hình zalopay
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
//        Log.d("Vao day", "onNewIntent: ve man hinh")
        paymentViewModel.handleZaloPayResult(
            intent = intent,
            onSuccess = {
                Log.d("ZaloPayResult", "Thanh toán thành công")
            },
            onError = {
                Log.e("ZaloPayResult", it)
            }
        )
    }
}


@Composable
fun MyApp(
    viewModel: UserViewModel,
    paymentViewModel: PaymentViewModel,
    chatViewModel: ChatViewModel,
    donViewModel: DonHangViewModel
) {

    val navController = rememberNavController()
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val systemUiController = rememberSystemUiController()
    // Thay đổi màu sắc StatusBar và NavigationBar dựa trên chế độ sáng/tối
    LaunchedEffect(currentRoute, isDarkTheme) {
        if (currentRoute != "welcome") { // Kiểm tra nếu không phải màn hình Welcome
            systemUiController.setStatusBarColor(
                color = if (isDarkTheme) Color(0xff898989) else Color.White,
                darkIcons = !isDarkTheme
            )
        } else {
            // Có thể đặt màu mặc định cho màn hình Welcome nếu cần
            systemUiController.setStatusBarColor(
                color = Color(0xFFF8774A), // Hoặc màu phù hợp cho màn Welcome
                darkIcons = true
            )
        }
    }
    PolyLapTopTheme(
        viewModel = viewModel
    ) {
        NavHost(navController = navController, startDestination = Screen.Welcome.route) {
            // Màn hình chào mừng
            composable(Screen.Welcome.route) {
                WelcomeScreen(onContinue = {
                    navController.navigate(Screen.BottomNav.route)
                }, viewModel)
            }
            composable(route = Screen.ChatScreen.route) {
                ChatScreen(navController, chatViewModel, viewModelUser = viewModel)
            }
            composable(
                route = Screen.DanhGia.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                DanhgiaScreenBasic(navController, viewModelUser = viewModel)
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
                    chiTietSanPhamJson = chiTietSanPhamJson,
                    viewModel = viewModel
                )
            }
            composable(
                route = Screen.OrderDetailsScreen.route + "/{chiTietSanPhamJson}",
                arguments = listOf(navArgument("chiTietSanPhamJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedJson = backStackEntry.arguments?.getString("chiTietSanPhamJson")
                val decodedJson = try {
                    encodedJson?.let { URLDecoder.decode(it, "UTF-8") }
                } catch (e: Exception) {
                    Log.e("OrderDetailsScreen", "Error decoding JSON: ${e.message}")
                    null
                }
                OrderDetailsScreen(
                    navController = navController,
                    chiTietSanPhamJson = decodedJson,
                    viewModel = paymentViewModel,
                    viewModelUser = viewModel
                )
            }
            composable(
                Screen.SearchScreen.route,
                enterTransition = {
                    fadeIn(animationSpec = tween(500)) + slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(500)
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(500)) + slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(500)
                    )
                },
            ) {
                SearchScreen(navController, viewModelUser = viewModel)
            }

            composable(Screen.DoiMatKhau.route) {
                DoiMatKhau(navController, viewModel)
            }



            composable(Screen.ThongTinCaNhan.route) {
                ThongTinCaNhan(navController, viewModel)
            }


            // Điều hướng Bottom Navigation với Scaffold và navController mới
            composable(Screen.BottomNav.route) {
                val bottomNavController =
                    rememberNavController() // NavController cho BottomNavigation

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
                                mainNavController = navController,
                                viewModelUser = viewModel
                            )
                        }
                        composable(BottomNavItem.Cart.route) {
                            CartScreen(
                                bottomNavController,
                                mainNavController = navController,
                                viewModel
                            )
                        }
                        composable(BottomNavItem.Order.route) {
                            OrderScreen(
                                bottomNavController = bottomNavController,
                                mainNavController = navController, donViewModel,
                                viewModel
                            )
                        }

                        composable(BottomNavItem.Setting.route) {
                            SettingScreen(
                                bottomNavController = bottomNavController,
                                mainNavController = navController,
                                viewModel
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
}

