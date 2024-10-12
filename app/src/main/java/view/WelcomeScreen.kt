package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onNavigate: () -> Unit) {
    // Giao diện cho màn hình welcome
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to My App", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        // Bạn có thể thêm hình ảnh hoặc các thành phần khác ở đây
    }

    // Sử dụng LaunchedEffect để điều hướng sau 3 giây
    LaunchedEffect(Unit) {
        delay(3000) // Delay 3 giây
        onNavigate() // Gọi hàm điều hướng
    }
}