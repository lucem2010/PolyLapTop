package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bottomnavigation.ScreenBottomNavigation.HomeScreen
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.polylaptop.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(navController: NavController? = null) {
    var selectedTab by remember { mutableStateOf(0) }
    var visible by remember { mutableStateOf(false) }
    var isLogoVisible by remember { mutableStateOf(false) } // New state for logo visibility

    // Danh sách tên các tab
    val tabTitles = listOf("Đăng Nhập", "Đăng Ký")

    val alphaAnimation = remember { Animatable(0f) }
    val yAnimation = remember { Animatable(1000f) }

    // State to track when the initial animation finishes
    var isInitialAnimationFinished by remember { mutableStateOf(false) }

    // Infinite scale transition that starts after the initial animation
    var isScaled by remember { mutableStateOf(false) } // State to control scalin
    val scale by animateFloatAsState(
        targetValue = if (isScaled) 1f else 0.5f, // 1.5f for zoom in effect
        animationSpec = tween(durationMillis = 1000) // Duration of the animation
    )

    val logoUrl =
        "https://vuainnhanh.com/wp-content/uploads/2023/02/logo-FPT-Polytechnic-.png"
    val logoAlpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        // Set isScaled to true after a short delay to start the scaling effect
        delay(500) // Optional delay before the scaling starts
        isScaled = true
    }
    // Initial animation for alpha and translationY
    LaunchedEffect("animationKey") {
        alphaAnimation.animateTo(1f, animationSpec = tween(1000))
        yAnimation.animateTo(700f, animationSpec = tween(500)) // ====upwards from 1000f to 500f
        yAnimation.animateTo(600f, animationSpec = tween(500))
        yAnimation.animateTo(500f, animationSpec = tween(500)) // Continue moving upwards to 100f
        yAnimation.animateTo(400f, animationSpec = tween(500)) // Continue moving upwards to 100f
        yAnimation.animateTo(300f, animationSpec = tween(500)) // Continue moving upwards to 100f
        yAnimation.animateTo(200f, animationSpec = tween(500)) // Continue moving upwards to 100f
        yAnimation.animateTo(100f, animationSpec = tween(500))   // End at the top (or 0f)

        isInitialAnimationFinished = true
    }


    LaunchedEffect(Unit) {
        delay(700)
        visible = true // Đặt visible thành true sau khi trì hoãn
    }

    if(visible){
        scope.launch {
            delay(500) // Adjust delay for when the logo should appear
            logoAlpha.animateTo(
                1f,
                animationSpec = tween(durationMillis = 500)
            ) // Animate logo appearance
            isLogoVisible = true // Set logo visibility to true
        }

    }

    // UI with both animations applied to a single Text composable

    // Giao diện tổng thể
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFC720D)),
        

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Add horizontal padding if needed
            horizontalArrangement = Arrangement.SpaceBetween, // Space out the two images
            verticalAlignment = Alignment.CenterVertically // Align them vertically in the center
        ) {

            // Right-side arrow icon pointing left
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .size(28.dp) // Size for the arrow icon
                    .clickable {
                        // Action when the arrow is clicked, e.g., navigate back
                        navController?.popBackStack()
                    }
            )
            // Left-side logo image
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(logoUrl)
                        .build()
                ),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp) // Set logo size
                    .graphicsLayer(alpha = logoAlpha.value) // Animate alpha for fade-in effect
                    .padding(top = 16.dp) // Optional padding
            )


        }
        Column(
            modifier = Modifier
                .width(400.dp)
                .height(600.dp)
                .padding(5.dp)
                .padding(top = 110.dp)

                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            // Tiêu đề
            AnimatedVisibility(
                visible = visible,
                // Sets the initial height of the content to 20, revealing only the top of the content at
                // the beginning of the expanding animation.
                enter = expandVertically(expandFrom = Alignment.Top) { 20 },
                // Shrinks the content to half of its full height via an animation.
                exit = shrinkVertically(animationSpec = tween()) { fullHeight -> fullHeight / 2 },
            ) {
                // Content that needs to appear/disappear goes here:


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = 8.dp, // Bóng đổ cho Card
                    shape = RoundedCornerShape(16.dp) // Bo góc cho Card
                ) {
                    Column(

                    ) {


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

                        // Thêm đường kẻ phân cách thứ hai
                    }
                }
            }


        }
        // Hiệu ứng tên lửa

        Box(
            modifier = Modifier
                .fillMaxWidth() // Chiếm toàn bộ chiều rộng của màn hình
                .padding(16.dp), // Thêm khoảng cách nếu cần
            contentAlignment = Alignment.Center // Căn giữa cả chiều ngang và dọc
        ) {
            Text(
                text = "Poly Laptop",
                fontSize = 36.sp, // Kích thước chữ
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale, // Sử dụng giá trị scale
                        scaleY = scale, // Sử dụng giá trị scale
                        alpha = alphaAnimation.value,
                        translationY = yAnimation.value
                    )
                    .clickable {
                        selectedTab = if (selectedTab == 0) 1 else 0
                    }
            )
        }


    }
}

@Preview
@Composable
private fun showAuthScreen() {
    AuthScreen();
}

