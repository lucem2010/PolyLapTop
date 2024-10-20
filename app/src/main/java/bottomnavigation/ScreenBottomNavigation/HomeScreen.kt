package bottomnavigation.ScreenBottomNavigation

import android.widget.Toast
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController? = null) {
    val context = LocalContext.current // Lấy ngữ cảnh
    var isClicked by remember { mutableStateOf(false) }
    var currentStep by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val bannerList = listOf(
        R.drawable.banner,
        R.drawable.macbook,
        R.drawable.banner
    )

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFFD9D9D9))
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
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        content = { paddingValues ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color(0xFFD9D9D9))
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (navController != null) {
                                navController.navigate("authScreen")
                            } else {
                                Toast.makeText(context, "Navigation controller is null", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC221F)
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

                    Spacer(modifier = Modifier.width(2.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

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
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items(bannerList.size) { index ->
                        BannerItem(imageResId = bannerList[index])
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bannerList.forEachIndexed { index, _ ->
                        DotIndicator(isSelected = currentStep == index)
                    }
                }

                LaunchedEffect(currentStep) {
                    delay(3000)
                    currentStep = (currentStep + 1) % bannerList.size
                    listState.animateScrollToItem(currentStep)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Phần nội dung vuốt được bắt đầu từ đây
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Tiêu đề "Mới" và danh sách ngang
                    item {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Mới",
                                    fontSize = 25.sp,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                items(4) {
                                    ProductItem()
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
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
                            ProductItem()

                            if (index + 1 < 10) {
                                ProductItem()
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
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
            painter = painterResource(id = R.drawable.macbook),
            contentDescription = "Product Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Macbook",
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
            .border(2.dp, Color.Gray, RoundedCornerShape(5.dp))
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(5.dp))
        )
    }
}

@Composable
fun DotIndicator(isSelected: Boolean) {
    val color = if (isSelected) Color.Blue else Color.Gray
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color, shape = CircleShape)
    )
}

@Preview
@Composable
private fun show() {
    HomeScreen()
}