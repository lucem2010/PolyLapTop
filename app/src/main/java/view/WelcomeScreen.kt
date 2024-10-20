package view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.polylaptop.R
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onNavigate: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    val welcomeTexts = listOf(
        "Welcome to My App",
        "Dedicated to Providing Quality Service",
        "Get Ready to Elevate Your Service Experience"
    )

    // State cho hiệu ứng fade-in
    val fadeInAnimation = remember { Animatable(0f) }

    // Giao diện cho màn hình welcome
    LaunchedEffect(Unit) {
        fadeInAnimation.animateTo(1f, animationSpec = tween(durationMillis = 2500, easing = LinearEasing))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF4B3A)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        // Hiệu ứng cho hình ảnh logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Image",
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer(alpha = fadeInAnimation.value), // Hiệu ứng fade-in
            contentScale = ContentScale.Crop
        )

        // Hộp chứa văn bản
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            val parts = welcomeTexts[currentStep].split("to ")
            // Văn bản "Welcome"
            Text(
                text = parts[0],
                fontSize = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer(alpha = fadeInAnimation.value) // Hiệu ứng fade-in
            )

            // Văn bản "to"
            Text(
                text = "to",
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .graphicsLayer(alpha = fadeInAnimation.value) // Hiệu ứng fade-in
            )

            // Văn bản "PolyLaptop"
            Text(
                text = parts[1],
                fontSize = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer(alpha = fadeInAnimation.value) // Hiệu ứng fade-in
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Nút Skip
            Button(
                onClick = {
                    currentStep = (currentStep + 1) % welcomeTexts.size
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                (0..2).forEach { index ->
                    DotIndicator(isSelected = currentStep == index)
                }
            }
        }


    }

    // Sử dụng LaunchedEffect để điều hướng tự động sau 3 giây
    LaunchedEffect(currentStep) {
        delay(3000)  // Delay 3 giây
        currentStep = (currentStep + 1) % welcomeTexts.size
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
            .size(5.dp)
            .background(color, shape = CircleShape) // Đường viền tròn cho chấm
    )
}
