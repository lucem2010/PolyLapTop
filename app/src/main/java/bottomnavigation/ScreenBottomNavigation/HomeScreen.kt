package bottomnavigation.ScreenBottomNavigation

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.polylaptop.R
import kotlinx.coroutines.delay
import model.ChiTietSanPham
import model.SanPham
import model.Screen
import model.toJson
import viewmodel.SanPhamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bottomNavController: NavController,
    mainNavController: NavController,
    viewModel: SanPhamViewModel = viewModel()
) {
    val ipAddress = "http://192.168.16.104:5000"
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

    // Lấy dữ liệu từ viewModel
    val sanPhamList by viewModel.sanPhamList.observeAsState(emptyList())
    val chiTietSanPhamMap by viewModel.chiTietSanPhamMap.observeAsState(emptyMap())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState("")

    // Gọi fetchSanPham() khi màn hình được mở
    LaunchedEffect(Unit) {
        viewModel.fetchSanPham()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Thanh tiêu đề
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Tiêu đề trên màn hình
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
                    onClick = { mainNavController.navigate(Screen.Auth.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .height(35.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Banner
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = bannerList[currentImageIndex],
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }

            // Thanh tìm kiếm
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text(text = "Tìm kiếm") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(100.dp))
                    .background(Color.White, shape = RoundedCornerShape(100.dp)),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray)
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter Icon", tint = Color.Gray)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Nội dung phần sản phẩm mới
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AnimatedDivider(
                    isVisible = isRedAndVisible,
                    startToEnd = true,
                    color = Color(0xFFFFA500)
                )
                Text(
                    text = "New",
                    fontSize = 22.sp,
                    color = textColor,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )

                if (isLoading) {
                    CircularProgressIndicator() // Hoặc bạn có thể dùng ProgressBar
                } else if (errorMessage.isNotEmpty()) {
                    Text("Lỗi: $errorMessage", color = Color.Red)
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        sanPhamList?.let { list ->
                            items(list) { sanPham ->
                                NewProductItem(
                                    navController = mainNavController,
                                    sanPham = sanPham,
                                    chiTietSanPhamMap,
                                    ipAddress
                                )
                            }
                        }
                    }
                }

                // Nội dung sản phẩm phổ biến
                AnimatedDivider(
                    isVisible = isRedAndVisible,
                    startToEnd = true,
                    color = Color(0xFFFFA500)
                )

                Text(
                    text = "Phổ biến",
                    fontSize = 25.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Lưới sản phẩm phổ biến
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    items(sanPhamList.size) { index -> // Sử dụng index để truy cập phần tử
                        val sanPham = sanPhamList[index]
                        ProductItem(
                            navController = mainNavController,
                            sanPham = sanPham,
                            chiTietSanPhamMap = chiTietSanPhamMap,
                            ipAddress = ipAddress
                        )
                    }
                }


            }
        }
    }
}






@Composable
fun NewProductItem(navController: NavController, sanPham: SanPham,
                   chiTietSanPhamMap: Map<String, List<ChiTietSanPham>>,ipAddress: String) {
    // Địa chỉ IP của mạng
    val chiTietSanPhamList = chiTietSanPhamMap[sanPham._id]
    val chiTietSanPham = chiTietSanPhamList?.firstOrNull()

    // Chuyển đổi đối tượng thành chuỗi JSON
    val sanPhamJson = Uri.encode(toJson(sanPham))
    val chiTietSanPhamListJson = Uri.encode(toJson(chiTietSanPhamList ?: emptyList()))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .shadow(8.dp, RoundedCornerShape(6.dp))
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .clickable {  navController.navigate(
                Screen.ProductDetail.route+"/${sanPhamJson}/${chiTietSanPhamListJson}"
            ) }
    ) {
        // Lấy phần tử đầu tiên trong danh sách anhSP, nếu có
        val imageUrl = sanPham.anhSP?.firstOrNull()?.let { "$ipAddress$it" }

        if (imageUrl != null) {
            // Hiển thị hình ảnh từ URL của sản phẩm (nếu có)
            AsyncImage(
                model = imageUrl,
                contentDescription = sanPham.tenSP,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            )
        } else {
            // Hình ảnh mặc định nếu không có URL
            Image(
                painter = painterResource(id = R.drawable.macbook),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hiển thị tên sản phẩm
            Text(
                text = sanPham.tenSP,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

//            // Hiển thị giá sản phẩm (cần thêm thuộc tính giá vào model SanPham nếu chưa có)
//            Text(
//                text = "${sanPham.gia}Đ",
//                fontSize = 13.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Red,
//                textDecoration = TextDecoration.LineThrough
//            )
        }
    }
}






@Composable
fun ProductItem(
    navController: NavController,
    sanPham: SanPham,
    chiTietSanPhamMap: Map<String, List<ChiTietSanPham>>,
    ipAddress: String
) {
    // Lấy danh sách ChiTietSanPham từ chiTietSanPhamMap theo id sản phẩm
    val chiTietSanPhamList = chiTietSanPhamMap[sanPham._id]
    val chiTietSanPham = chiTietSanPhamList?.firstOrNull()

    // Chuyển đổi đối tượng thành chuỗi JSON
    val sanPhamJson = Uri.encode(toJson(sanPham))
    val chiTietSanPhamListJson = Uri.encode(toJson(chiTietSanPhamList ?: emptyList()))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(150.dp)
            .shadow(8.dp, RoundedCornerShape(6.dp))
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .clickable {  navController.navigate(
                Screen.ProductDetail.route+"/${sanPhamJson}/${chiTietSanPhamListJson}"
            ) }
    ) {
        val imageUrl = sanPham.anhSP?.firstOrNull()?.let { "$ipAddress$it" }
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = sanPham.tenSP,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.macbook),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            )
        }

        Text(
            text = sanPham.tenSP,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        chiTietSanPham?.Gia?.let {
            Text(
                text = "$it Đ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
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
