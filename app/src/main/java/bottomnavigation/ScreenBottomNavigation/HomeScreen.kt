package bottomnavigation.ScreenBottomNavigation

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.polylaptop.R
import kotlinx.coroutines.delay
import model.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bottomNavController: NavController,
    mainNavController: NavController
) {
    val imgLogo = "https://vuainnhanh.com/wp-content/uploads/2023/02/logo-FPT-Polytechnic-.png"
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val bannerList = listOf(
        "https://i.pinimg.com/564x/34/dd/68/34dd681c1fe679f5f40b46199e324b6b.jpg",
        "https://i.pinimg.com/564x/9a/13/dc/9a13dc79ca4368d6c87acb2e52cadf9d.jpg",
        "https://i.pinimg.com/736x/6a/4a/92/6a4a9250679050818495017601ae0d63.jpg"
    )


    var currentImageIndex by remember { mutableStateOf(0) }
    var isRedAndVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            isRedAndVisible = !isRedAndVisible

        }
    }

    val textColor by animateColorAsState(if (isRedAndVisible) Color.Red else Color.Black)

    // Automatically switch images at intervals
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // 3-second delay for each image
            currentImageIndex = (currentImageIndex + 1) % bannerList.size
        }
    }


    // Khởi tạo Box để chứa các thành phần chính
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Thanh tiêu đề


        // Nội dung chính
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()

        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFFC720D)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imgLogo,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(50.dp), // Set size of the image
                    contentScale = ContentScale.Fit // Scale the image to fit within the size
                )

                Text(
                    text = "Poly Laptop",
                    fontSize = 25.sp, // Kích thước chữ
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Button(
                    onClick = {
                        mainNavController.navigate(Screen.Auth.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent // Background color of the button
                    ),
                    modifier = Modifier
                        .height(35.dp)
                        .clip(RoundedCornerShape(20.dp)), // Clip the button to have rounded corners
                    shape = RoundedCornerShape(20.dp) // Optional if you want to define shape separately
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontSize = 16.sp, // Slightly larger font size for better readability
                        fontWeight = FontWeight.Bold // Bold text for emphasis
                    )
                }


            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = bannerList[currentImageIndex], // Load image from URL
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth() // Make image fill the available width
                        .height(160.dp) // Set the height to 100 dp
                )
            }

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(text = "Tìm kiếm") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .background(Color.White, shape = RoundedCornerShape(100.dp)),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter Icon",
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            // Handle filter click action
                        }
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            // Phần nội dung vuốt được bắt đầu từ đây
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Tiêu đề "Mới" và danh sách ngang
                item {
                    Column {
                        AnimatedDivider(
                            isVisible = isRedAndVisible,
                            startToEnd = true, // Kéo dài từ trái qua phải
                            color = Color(0xFFFFA500)
                        )
                        Text(
                            text = "New",
                            fontSize = 22.sp,
                            color = textColor,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .graphicsLayer() { // Hiệu ứng thu nhỏ hoặc phóng to khi cuộn
                                    scaleX = 0.95f
                                    scaleY = 0.95f
                                },
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(4) {
                                NewProductItem(navController = mainNavController)
                            }
                        }

                        AnimatedDivider(
                            isVisible = isRedAndVisible,
                            startToEnd = true, // Kéo dài từ trái qua phải
                            color = Color(0xFFFFA500)
                        )


                    }
                }

                // Tiêu đề "Phổ biến" và danh sách sản phẩm dạng lưới
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Phổ biến",
                            fontSize = 25.sp,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(10) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProductItem(navController = mainNavController)

                        if (index + 1 < 10) {
                            ProductItem(navController = mainNavController)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}


@Composable
fun NewProductItem(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .shadow(8.dp, RoundedCornerShape(6.dp))
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .clickable { navController.navigate(Screen.ProductDetail.route) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.macbook),
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 6.dp
                    )
                ) // Bo góc trên của hình ảnh
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Macbook",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,

                )

            Text(
                text = "300000Đ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                textDecoration = TextDecoration.LineThrough // Hiệu ứng gạch ngang
            )


        }

    }
}


@Composable
fun ProductItem(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(150.dp)
            .shadow(8.dp, RoundedCornerShape(6.dp))
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .clickable { navController.navigate(Screen.ProductDetail.route) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.macbook),
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 6.dp
                    )
                ) // Bo góc trên của hình ảnh
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Macbook",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,

                )

            Text(
                text = "300000Đ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                textDecoration = TextDecoration.LineThrough // Hiệu ứng gạch ngang
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "300000Đ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,

                    )
                Text(
                    text = "329",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,

                    )
            }


        }

    }
}


@Composable
fun AnimatedDivider(
    isVisible: Boolean,
    startToEnd: Boolean,
    color: Color
) {
    // Điều chỉnh chiều rộng từ 0.dp đến chiều rộng của màn hình
    val width by animateDpAsState(targetValue = if (isVisible) LocalConfiguration.current.screenWidthDp.dp else 0.dp)

    // Căn giữa hoặc trái phải dựa vào hướng kéo dài
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(2.dp) // Chiều cao của Divider
    ) {
        Divider(
            color = color,
            thickness = 2.dp,
            modifier = Modifier
                .width(width)
                .align(if (startToEnd) Alignment.CenterStart else Alignment.CenterEnd) // Căn trái hoặc phải
        )
    }
}
