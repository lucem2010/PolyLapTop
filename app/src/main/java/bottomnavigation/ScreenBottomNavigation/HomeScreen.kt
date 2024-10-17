package bottomnavigation.ScreenBottomNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.polylaptop.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController? = null) {
    var isClicked by remember { mutableStateOf(false) } // Biến trạng thái để theo dõi khi nhấn vào "thêm"
    var currentStep by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) } // Biến trạng thái cho TextField

    // Sử dụng danh sách tài nguyên hình ảnh thay vì BannerItem
    val bannerList = listOf(
        R.drawable.banner,  // Thay thế bằng các tài nguyên hình ảnh banner của bạn
        R.drawable.macbook,
        R.drawable.banner
    )

    // Tạo LazyListState để điều khiển cuộn
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            Box(
                contentAlignment = Alignment.Center, // Căn chỉnh chính giữa
                modifier = Modifier
                    .background(Color(0xFFD9D9D9)) // Màu nền tùy chỉnh là D9D9D9
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 15.dp, end = 15.dp)
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                Text(
                    text = "POLY LAPTOP",
                    fontSize = 26.sp,
                    color = Color.Black,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold // In đậm text
                    )
                )
            }
        },
        content = { paddingValues ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color(0xFFD9D9D9)) // Màu nền tùy chỉnh là D9D9D9
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(10.dp)) // Tạo khoảng cách giữa topbar và button

                // Row chứa hai nút ngang hàng, căn sang phải
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    horizontalArrangement = Arrangement.End, // Căn sang phải
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút 1
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC221F) // Màu nền của nút
                        ),
                        modifier = Modifier
                            .height(50.dp)
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(35.dp),
                                color = Color.Black
                            ),
                        shape = RoundedCornerShape(35.dp)
                    ) {
                        Text(
                            text = "Login",
                            color = Color.White,
                            fontSize = 15.sp,
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp)) // Thêm khoảng cách giữa hai nút

                    // Nút 2
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC221F) // Màu nền của nút
                        ),
                        modifier = Modifier
                            .height(50.dp)
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(35.dp),
                                color = Color.Black
                            ),
                        shape = RoundedCornerShape(35.dp)
                    ) {
                        Text(
                            text = "Register",
                            color = Color.White,
                            fontSize = 15.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Thanh tìm kiếm
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text(text = "Tìm kiếm") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp)
                        .border(
                            width = 2.dp, // Độ dày của viền
                            color = Color.Gray, // Màu của viền
                            shape = RoundedCornerShape(100.dp) // Độ bo góc
                        )
                        .background(Color.White, shape = RoundedCornerShape(100.dp)), // Nền trắng và bo góc
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Đặt màu nền bên trong là trong suốt để sử dụng background ở ngoài
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Hiển thị các banner có thể vuốt ngang
                LazyRow(
                    state = listState, // Áp dụng trạng thái cuộn
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items(bannerList.size) { index ->
                        BannerItem(imageResId = bannerList[index])
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Chấm chỉ thị cho vị trí banner hiện tại
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bannerList.forEachIndexed { index, _ ->
                        DotIndicator(isSelected = currentStep == index)
                    }
                }

                // Tự động chuyển banner sau mỗi 3 giây
                LaunchedEffect(currentStep) {
                    delay(3000)
                    currentStep = (currentStep + 1) % bannerList.size
                    listState.animateScrollToItem(currentStep) // Cuộn đến banner tiếp theo
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nội dung có thể cuộn từ đây trở xuống
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Đảm bảo LazyColumn chiếm hết không gian cuộn còn lại
                ) {
                    item {
                        // Nội dung tiếp theo (Danh sách sản phẩm)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween, // Căn giữa Phổ biến và Thêm
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Phổ biến",
                                fontSize = 25.sp,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold // In đậm text
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                modifier = Modifier
                                    .wrapContentSize() // Giới hạn vùng nhấn chỉ bao quanh text
                                    .clickable {
                                        isClicked = !isClicked // Thay đổi trạng thái khi nhấn
                                    },
                                text = "Thêm>>",
                                fontSize = 18.sp
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))

                        // Danh sách sản phẩm theo chiều ngang
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(4) {
                                ProductItem()
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween, // Căn giữa Phổ biến và Thêm
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mới",
                                fontSize = 25.sp,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold // In đậm text
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clickable {
                                        isClicked = !isClicked
                                    },
                                text = "Thêm>>",
                                fontSize = 18.sp
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))

                        // Danh sách sản phẩm theo chiều ngang
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(4) {
                                ProductItem()
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ProductItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
            .background(Color.White, shape = RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.macbook), // Thay thế bằng hình ảnh sản phẩm thực tế của bạn
            contentDescription = "Product Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Macbook", // Tên sản phẩm
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun BannerItem(imageResId: Int) {
    Box(
        modifier = Modifier
            .width(350.dp)
            .height(150.dp)
            .padding(8.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Banner",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
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

@Preview
@Composable
private fun show() {
    HomeScreen()
}