package bottomnavigation.ScreenBottomNavigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.polylaptop.R
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import model.AppConfig
import model.ChiTietSanPham
import model.HangSP
import model.Review
import model.SanPham
import model.Screen
import model.SharedPrefsManager
import model.imagesItem
import view.OrderDetailsScreen
import view.alert_dialog.CartIconWithLoginCheck
import viewmodel.CartViewModel
import viewmodel.SanPhamViewModel
import java.net.URLDecoder
import java.net.URLEncoder


@Composable
fun ProductDetail(   navController: NavController,
                     chiTietSanPhamJson: String?,
                     cartViewModel: CartViewModel= viewModel(),
                     sanPhamViewModel: SanPhamViewModel= viewModel()
) {


    val showDialogState = remember { mutableStateOf(false) }

    var isReviewDropdownVisible by remember { mutableStateOf(false) }

    // Giải mã và chuyển đổi chuỗi JSON thành đối tượng `ChiTietSanPham`
    val chiTietSanPham = chiTietSanPhamJson?.let {
        val decodedJson = URLDecoder.decode(it, "UTF-8")
        Log.d("ProductDetail", "Decoded chiTietSanPhamJson: $decodedJson") // Log JSON sau khi decode
        try {
            val chiTiet = Json.decodeFromString<ChiTietSanPham>(decodedJson)
            Log.d("ProductDetail", "ChiTietSanPham decoded successfully: $chiTiet") // Log đối tượng ChiTietSanPham
            chiTiet
        } catch (e: Exception) {
            Log.e("ProductDetail", "Error decoding ChiTietSanPham: ${e.message}", e)
            null
        }
    }


    val chiTietSanPhamList by sanPhamViewModel.chiTietSanPhamList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        if (chiTietSanPham != null) {
            sanPhamViewModel.fetchChiTietSanPhamOfid(chiTietSanPham.idSanPham._id)
        }

    }


    val sanPham = chiTietSanPham?.idSanPham?.let {
        SanPham(
            _id = it._id,
            idHangSP = HangSP(
                _id = it.idHangSP._id,
                TenHang = it.idHangSP.TenHang
            ),
            tenSP = it.tenSP,
            anhSP = it.anhSP
        )
    }


    // Sử dụng ảnh đầu tiên làm ảnh chính, nếu có
    val ipAddress =  AppConfig.ipAddress

// Sử dụng ảnh đầu tiên làm ảnh chính, nếu có
    val selectedImage = remember {
        mutableStateOf(sanPham?.anhSP?.firstOrNull()?.let { "$ipAddress$it" } ?: "")
    }

    // Log giá trị

    val imageScrollState = rememberScrollState()

    chiTietSanPhamList?.let { list ->
        println("Chi tiết sản phẩm: ${list.joinToString { it.MoTa }}")
    }

    var selectedRam by remember { mutableStateOf(chiTietSanPham?.Ram) }
    var selectedSSD by remember { mutableStateOf(chiTietSanPham?.SSD) }
    var selectedManHinh by remember { mutableStateOf(chiTietSanPham?.ManHinh) }
    var selectedMauSac by remember { mutableStateOf(chiTietSanPham?.MauSac) }


    // Lọc các lựa chọn từ danh sách chi tiết sản phẩm
    val ramOptions = chiTietSanPhamList?.map { it.Ram }?.distinct() ?: emptyList()
    val ssdOptions = chiTietSanPhamList?.map { it.SSD }?.distinct() ?: emptyList()
    val manHinhOptions = chiTietSanPhamList?.map { it.ManHinh }?.distinct() ?: emptyList()
    val mauSacOptions = chiTietSanPhamList?.map { it.MauSac }?.distinct() ?: emptyList()

    var chiTietSanPhamMap by remember { mutableStateOf<Map<String, Pair<Double, ChiTietSanPham>>>(emptyMap()) }
    var showOrderDetails by remember { mutableStateOf(false) }
    // Hàm để lọc chi tiết sản phẩm
    val filteredProducts = chiTietSanPhamList?.filter {
        (selectedRam == null || it.Ram == selectedRam) &&
                (selectedSSD == null || it.SSD == selectedSSD) &&
                (selectedManHinh == null || it.ManHinh == selectedManHinh) &&
                (selectedMauSac == null || it.MauSac == selectedMauSac)
    }

// Kiểm tra nếu có sản phẩm phù hợp
    val selectedProduct = filteredProducts?.firstOrNull { it._id == chiTietSanPham?._id }



    val imgLogo = "https://vuainnhanh.com/wp-content/uploads/2023/02/logo-FPT-Polytechnic-.png"

    val reviewData = listOf(
        Review(
            "User 1",
            "2024-11-07",
            5,
            "Great product, highly recommended!Great product, highly recommended!Great product, highly recommended!"
        ),
        Review(
            "User 2",
            "2024-11-06",
            4,
            "Fast delivery and good packaging.Great product, highly recommended!Great product, highly recommended!"
        ),
        Review(
            "User 3",
            "2024-11-05",
            3,
            "Not satisfied with the quality.Great product, highly recommended!Great product, highly recommended!"
        ),
        Review(
            "User 4",
            "2024-11-04",
            5,
            "Amazing service, will buy again!Great product, highly recommended!Great product, highly recommended!Great product, highly recommended!Great product, highly recommended!"
        ),

        )

    val context = LocalContext.current
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff)),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {

        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 30.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .size(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.left),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Chi tiết sản phẩm",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                AsyncImage(
                    model = imgLogo,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(50.dp), // Set size of the image
                    contentScale = ContentScale.Fit // Scale the image to fit within the size
                )

            }
        }

        // Product Image
        item {
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
            ) {
                // Hiển thị ảnh chính
                // Lấy URL của ảnh từ `selectedImage.value`, nếu có
                val imageUrl = selectedImage.value.takeIf { it.isNotEmpty() }

                if (imageUrl != null) {
                    // Hiển thị hình ảnh từ URL
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                BorderStroke(2.dp, Color(0x809C7056)),
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }


                Spacer(modifier = Modifier.height(10.dp))

                // Hiển thị ảnh bổ sung
                sanPham?.anhSP?.let { imagesList ->
                    Row(
                        modifier = Modifier
                            .horizontalScroll(imageScrollState)
                    ) {
                        imagesList.forEach { imagePath ->
                            // Thêm ipAddress vào mỗi URL hình ảnh
                            val imageUrl = "$ipAddress$imagePath"

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                                    .clickable {
                                        // Thay đổi ảnh chính khi nhấn vào ảnh phụ
                                        selectedImage.value = imageUrl
                                    }
                                    .clip(RoundedCornerShape(5.dp))
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .size(70.dp)
                                        .border(
                                            BorderStroke(
                                                2.dp,
                                                if (selectedImage.value == imageUrl) Color(0x809C7056) else Color(0xB3FFFFFF)
                                            ),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                )
                            }
                        }
                    }
                }


            }
        }

        // Product Information
        item {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
            ) {
                Text(
                    text = "Sản phẩm 1",
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = if (selectedProduct != null) {
                        "${selectedProduct.Gia} VND"
                    } else {
                        "Hết hàng"
                    },
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Right
                )
            }
        }



        // Dropdown cho thông số sản phẩm
        item {



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Dropdown cho RAM nếu có dữ liệu
                if (ramOptions.isNotEmpty()) {
                    DropdownOption("RAM", ramOptions, onItemSelected = { selectedRam = it })
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // Dropdown cho Màn hình nếu có dữ liệu
                if (manHinhOptions.isNotEmpty()) {
                    DropdownOption("Màn hình", manHinhOptions, onItemSelected = { selectedManHinh = it })
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // Dropdown cho SSD nếu có dữ liệu
                if (ssdOptions.isNotEmpty()) {
                    DropdownOption("Ổ cứng", ssdOptions, onItemSelected = { selectedSSD = it })
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // Dropdown cho Màu sắc nếu có dữ liệu
                if (mauSacOptions.isNotEmpty()) {
                    DropdownOption("Màu sắc", mauSacOptions, onItemSelected = { selectedMauSac = it })
                    Spacer(modifier = Modifier.height(5.dp))
                }

            }
        }

        // Mô tả sản phẩm
        item {
            Text(
                text = "Viverra auctor porta quam malesuada eu molestie dolor .molestie dolor  eu molestie dolor ...",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Justify,
                color = Color(0xB3000000),
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxHeight() // Áp dụng fillMaxHeight cho phần mô tả để không bị thiếu chữ
            )
        }

        // Nút thêm vào giỏ hàng
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {


                // Nút "Mua ngay"
                Button(
                    onClick = {
                        // Log trước khi thực hiện các bước
                        Log.d("ButtonClick", "Button clicked. Preparing data.")
                        if (token == null) {
                            showDialogState.value = true  // Hiển thị dialog
                        }  else{
                            val chiTietSanPhamMap = selectedProduct?.let {
                                mapOf(
                                    it._id to Pair(1.0, it)
                                )
                            } ?: emptyMap()

                            val chiTietSanPhamJson = Uri.encode(Gson().toJson(chiTietSanPhamMap))

                            try {
                                navController.navigate(Screen.OrderDetailsScreen.route + "/$chiTietSanPhamJson")
                            } catch (e: Exception) {
                                Log.e("ButtonClick", "Navigation error: ${e.message}")
                            }
                        }


                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedProduct != null) Color(0xFFFFA500) else Color.LightGray, // Màu cam nếu có sản phẩm, LightGray nếu không
                        contentColor = if (selectedProduct != null) Color.White else Color.Gray // Màu chữ trắng nếu có sản phẩm, màu xám nếu không
                    ),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(
                        "Mua ngay",
                        color = if (selectedProduct != null) Color.White else Color.Gray, // Màu chữ trắng nếu có sản phẩm, xám nếu không
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }



                    CheckLoginDialog(token, navController,showDialogState)


                CartIconWithLoginCheck(
                    navController = navController,
                    selectedProduct = selectedProduct,
                    cartViewModel
                )


            }
        }
        // Reviews Section
        item {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, end = 20.dp, start = 20.dp, bottom = 30.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                //Tao chu nghieng va gach chan
                AverageRatingRow(reviewData = reviewData)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        val text = "(${reviewData.size}+ Reviews)"
                        append(text)
                        addStyle(
                            style = SpanStyle(textDecoration = TextDecoration.Underline),
                            start = 0,
                            end = text.length
                        )
                    },
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.clickable {
                        isReviewDropdownVisible = !isReviewDropdownVisible
                    }
                )

            }
        }
        if (isReviewDropdownVisible) {
            items(reviewData) { review ->
                ReviewItem(review)
            }
        }

    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 20.dp)
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "Icon Person",
                tint = Color.Gray,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${review.username}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(review.rating) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Rating Star",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${review.date}",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = review.comment)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownOption(
    label: String,
    options: List<String>,
    onItemSelected: (String) -> Unit // Callback để xử lý lựa chọn của người dùng
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Label
            Text(
                text = "$label: ",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth() // Đảm bảo TextField chiếm toàn bộ chiều rộng
                    .height(55.dp)
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(12.dp) // Thêm border radius với shape tròn
                    )
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, // Đảm bảo background là trong suốt để sử dụng background của modifier
                    disabledTextColor = MaterialTheme.colors.onSurface,
                    focusedIndicatorColor = Color.White, // Ẩn bottom border khi TextField được chọn
                    unfocusedIndicatorColor = Color.White // Ẩn bottom border khi không được chọn
                ),
                shape = RoundedCornerShape(12.dp), // Thêm Border Radius cho TextField
                enabled = false, // Giữ trạng thái chỉ đọc
                textStyle = MaterialTheme.typography.body1.copy(
                    textAlign = TextAlign.End // Căn chỉnh văn bản sang bên phải
                ),
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onItemSelected(option) // Gọi callback khi người dùng chọn một giá trị
                    }
                ) {
                    Text(text = option, textAlign = TextAlign.End)
                }
            }
        }
    }
}


@Composable
fun AverageRatingRow(reviewData: List<Review>) {

    // Tính toán trung bình xếp hạng từ danh sách đánh giá
    val averageRating = calculateAverageRating(reviewData)
    // Lấy phần nguyên và phần thập phân của rating
    val fullStars = averageRating.toInt() // Số sao đầy đủ
    val isHalfStar = (averageRating % 1) != 0.0.toFloat() // Kiểm tra có sao lẻ hay không
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Hiển thị tổng trung bình dưới dạng số
        Text(
            text = "%.1f".format(averageRating),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (index in 1..5) {
                Icon(
                    imageVector = when {
                        index <= fullStars -> Icons.Rounded.Star // Sao đầy
                        index == fullStars + 1 && isHalfStar -> Icons.Rounded.StarHalf // Sao lẻ
                        else -> Icons.Rounded.StarOutline // Sao trống
                    },
                    contentDescription = null,
                    tint = Color.Blue, // Màu sao (bạn có thể tùy chỉnh theo ý muốn)
                    modifier = Modifier.size(20.dp) // Kích thước sao (tuỳ chỉnh nếu cần)
                )
            }
        }
    }
}

//tính xếp hạng trung bình từ danh sách đánh giá của bạn
fun calculateAverageRating(reviews: List<Review>): Float {
    return if (reviews.isNotEmpty()) {
        reviews.map { it.rating }.average().toFloat()
    } else {
        0f
    }
}



@Composable
fun CheckLoginDialog(
    token: String?,
    navController: NavController,
    showDialogState: MutableState<Boolean>
) {
    if (showDialogState.value) {
        AlertDialog(
            onDismissRequest = {
                showDialogState.value = false // Đóng dialog khi người dùng nhấn ngoài
            },
            title = { Text(text = "Bạn chưa đăng nhập") },
            text = { Text(text = "Bạn cần đăng nhập để tiếp tục.") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(Screen.Auth.route)
                        showDialogState.value = false // Đóng dialog sau khi chuyển đến màn hình đăng nhập
                    }
                ) {
                    Text("Đăng nhập")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialogState.value = false // Đóng dialog khi người dùng nhấn "Đóng"
                    }
                ) {
                    Text("Đóng")
                }
            }
        )
    }
}


// Hàm mở rộng để định dạng số thập phân
fun Float.format(digits: Int) = "%.${digits}f".format(this)

//@Composable
//@Preview(showBackground = true)
//private fun ProductDetailPreview() {
//    ProductDetail(navController = rememberNavController())
//}