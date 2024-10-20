package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AuthScreen(navController: NavController? = null) {
    var selectedTab by remember { mutableStateOf(0) }

    // Danh sách tên các tab
    val tabTitles = listOf("Đăng Nhập", "Đăng Ký")

    // Giao diện tổng thể
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // Màu nền tổng thể
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Thêm khoảng cách bên ngoài
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề
            Text(
                text = "Chào Mừng!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE), // Màu tiêu đề
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Khung cho TabRow với bo góc và hiệu ứng bóng đổ
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 8.dp, // Bóng đổ cho Card
                shape = RoundedCornerShape(16.dp) // Bo góc cho Card
            ) {
                Column {
                    // Tạo TabRow
                    TabRow(
                        selectedTabIndex = selectedTab,
                        backgroundColor = Color.White,
                        contentColor = Color(0xFF6200EE)
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.padding(8.dp) // Thêm khoảng cách cho tab
                            )
                        }
                    }

                    // Hiển thị nội dung dựa trên tab đã chọn
                    when (selectedTab) {
                        0 -> LoginScreen()
                        1 -> RegisterScreen()
                    }
                }
            }
        }
    }
}