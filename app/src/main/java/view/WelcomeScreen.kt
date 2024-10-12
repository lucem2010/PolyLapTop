package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onNavigate: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }

    // Danh sách văn bản cho các bước
    val welcomeTexts = listOf(
        "Welcome to My App",
        "Welcome to My App 1",
        "Welcome to My App 2"
    )

    // Giao diện cho màn hình welcome
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = welcomeTexts[currentStep], fontSize = 24.sp)

        // Hiển thị nút Skip
        Button(
            onClick = {
                // Chuyển sang bước tiếp theo hoặc quay lại bước đầu tiên nếu đã ở bước cuối
                currentStep = (currentStep + 1) % welcomeTexts.size
                // Nếu đã ở bước cuối cùng, điều hướng
                if (currentStep == 0) {
                    onNavigate()
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Skip")
        }

        // Hiển thị 3 dấu chấm bên dưới
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (0..2).forEach { index ->
                DotIndicator(isSelected = currentStep == index)
            }
        }
    }

    // Sử dụng LaunchedEffect để điều hướng tự động sau 3 giây
    LaunchedEffect(currentStep) {
        // Delay 3 giây
        delay(3000)
        // Chuyển sang bước tiếp theo
        currentStep = (currentStep + 1) % welcomeTexts.size
        // Nếu đã ở bước cuối cùng, điều hướng
        if (currentStep == 0) {
            onNavigate()
        }
    }
}

@Composable
fun DotIndicator(isSelected: Boolean) {
    val color = if (isSelected) Color.Blue else Color.Gray
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color, shape = CircleShape) // Đường viền tròn cho chấm
    )
}