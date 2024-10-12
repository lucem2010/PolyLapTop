package com.example.polylaptop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import bottomnavigation.BottomNavigationBar
import bottomnavigation.NavigationHost
import com.example.polylaptop.ui.theme.PolyLapTopTheme
import view.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PolyLapTopTheme {
                // State để quản lý điều hướng
                val startApp = remember { mutableStateOf(true) }
                val navController = rememberNavController()

                // Kiểm tra nếu đang trong màn hình welcome hay bottom navigation
                if (startApp.value) {
                    WelcomeScreen {
                        startApp.value = false // Đặt lại trạng thái để chuyển sang màn hình chính
                    }
                } else {
                    // Hiển thị Navigation Host với Bottom Navigation


                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) { innerPadding ->
                        // Nội dung chính của bạn sẽ ở đây
                        NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
                    }

                }
            }
        }
    }
}

