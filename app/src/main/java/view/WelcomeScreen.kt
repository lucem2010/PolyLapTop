package view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import bottomnavigation.ScreenBottomNavigation.ProductDetail
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Screen
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    var isCircleMode by remember { mutableStateOf(true) } // Set to true by default
    var isTextVisible by remember { mutableStateOf(false) } // New state for text visibility
    var isLogoVisible by remember { mutableStateOf(false) } // New state for logo visibility
    val radius = 150.dp // Bán kính cho vòng tròn cuối
    val imageUrls = listOf(
        "https://i.pinimg.com/564x/69/96/8a/69968a4397724231650c21084e06096f.jpg",
        "https://i.pinimg.com/564x/d6/78/cd/d678cdbea7a3f96dd33b713f50e5ec57.jpg",
        "https://i.pinimg.com/564x/9e/51/a8/9e51a8200285392a0ed56d62d9411ad8.jpg",
        "https://i.pinimg.com/564x/c0/ba/00/c0ba006064edc2543d1e7708fecc2fa2.jpg",
        "https://i.pinimg.com/564x/30/ab/5d/30ab5de83761dcad4a8a3273834a1df8.jpg",
        "https://i.pinimg.com/736x/de/ba/3e/deba3e97461ff22e3303e70e46a5206b.jpg",
        "https://i.pinimg.com/564x/19/3c/2d/193c2dc61869d9489df55404247dd6d4.jpg",
        "https://i.pinimg.com/564x/21/98/b3/2198b3ef96e2a1a7027664cd569d23ec.jpg"
    )

    val logoUrl =
        "https://vuainnhanh.com/wp-content/uploads/2023/02/logo-FPT-Polytechnic-.png" // Logo URL

    // Tạo danh sách Animatable cho vị trí x và y của mỗi hình ảnh
    val imageOffsetsX = remember {
        List(imageUrls.size) { Animatable(Random.nextFloat() * 600f - 300f) } // X offset
    }
    val imageOffsetsY = remember {
        List(imageUrls.size) { Animatable(-Random.nextFloat() * 500f) } // Y offset
    }
    val imageAlphaAnimations = remember {
        List(imageUrls.size) { Animatable(0f) }
    }

    val logoAlpha = remember { Animatable(0f) } // Animatable for logo visibility
    val letterOffsets =
        remember { List("Poly Laptop".length) { Animatable(-50f) } } // X offsets for letters
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current // Sử dụng LocalDensity để chuyển đổi Dp sang Px


    val buttonScale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        // Create an infinite loop for the pulsing effect
        while (true) {
            buttonScale.animateTo(
                targetValue = 1.1f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
            buttonScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }
    }

    LaunchedEffect(isCircleMode) {
        if (isCircleMode) {




            // Animate images to form a circle
            imageOffsetsX.forEachIndexed { index, offsetAnimX ->
                scope.launch {
                    val angle = (index * (2 * PI) / imageUrls.size).toFloat()
                    val targetX = with(density) { radius.toPx() * cos(angle) }
                    val targetY = with(density) { radius.toPx() * sin(angle) }

                    // Dần hiện ảnh
                    imageAlphaAnimations[index].animateTo(1f, tween(500))

                    // Di chuyển đến vị trí xếp thành hình tròn
                    offsetAnimX.animateTo(
                        targetX,
                        animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
                    )
                    imageOffsetsY[index].animateTo(
                        targetY,
                        animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
                    )
                }
            }

            // Wait for the images to finish forming
            delay(2500) // Adjust delay as necessary

            // After forming the circle, show the text
            isTextVisible = true

            // Animate letters for "Polylaptop" one by one
            "Poly Laptop".forEachIndexed { index, _ ->
                scope.launch {
                    // Add a delay for each letter to appear sequentially
                    delay(index * 300L) // Adjust timing for letter appearance
                    letterOffsets[index].animateTo(
                        0f,
                        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                    )
                }
            }

            // Animate the logo to appear after the text starts
            scope.launch {
                delay(2500) // Adjust delay for when the logo should appear
                logoAlpha.animateTo(
                    1f,
                    animationSpec = tween(durationMillis = 1000)
                ) // Animate logo appearance
                isLogoVisible = true // Set logo visibility to true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFC720D)),
        contentAlignment = Alignment.Center
    ) {
        // Vòng tròn hình ảnh di chuyển
        imageUrls.forEachIndexed { index, url ->
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp) // Đặt kích thước cho hình ảnh lớn hơn một chút
                    .graphicsLayer(
                        translationX = imageOffsetsX[index].value,
                        translationY = imageOffsetsY[index].value,
                        alpha = imageAlphaAnimations[index].value
                    )
            )
        }

        // Display the logo at the top, animated
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logoUrl)
                    .build()
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(100.dp) // Set logo size
                .align(Alignment.TopCenter)
                .graphicsLayer(alpha = logoAlpha.value) // Animate alpha for fade-in effect
                .padding(top = 16.dp) // Optional padding
        )

        // Display the text "Polylaptop" at the center only if it's visible
        if (isTextVisible) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center) // Center the Row
            ) {
                "Poly Laptop".forEachIndexed { index, letter ->
                    Text(
                        text = letter.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp, // Adjust font size as needed
                        modifier = Modifier
                            .offset(x = letterOffsets[index].value.dp)
                    )
                }
            }
        }
        Button(
            onClick =onContinue,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .scale(buttonScale.value) // Apply pulsing animation
                .shadow(elevation = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White) // Set button background color
        ) {
            Text(text = "Mua sắm", color = Color.Magenta)
            Text(text = " ngay", color = Color.Blue)
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun WelcomeScreenPreview() {
    WelcomeScreen(onContinue = {})
}


