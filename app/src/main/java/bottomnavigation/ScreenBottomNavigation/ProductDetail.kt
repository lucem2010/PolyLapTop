package bottomnavigation.ScreenBottomNavigation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.polylaptop.R
import kotlinx.serialization.json.Json
import model.ChiTietSanPham
import model.EncryptedPrefsManager
import model.Review
import model.SanPham
import model.imagesItem
import view.alert_dialog.CartIconWithLoginCheck
import java.net.URLDecoder


@Composable
fun ProductDetail(   navController: NavController,
                     sanPhamJson: String?,
                     chiTietSanPhamMapJson: String?) {



    var isReviewDropdownVisible by remember { mutableStateOf(false) }

    // Giải mã chuỗi JSON trước khi decode thành đối tượng
    val sanPham = sanPhamJson?.let {
        val decodedJson = URLDecoder.decode(it, "UTF-8")
        Log.d("ProductDetail", "Decoded sanPhamJson: $decodedJson") // Log JSON sau khi decode
        try {
            val sanPhamObj = Json.decodeFromString<SanPham>(decodedJson)
            Log.d("ProductDetail", "SanPham decoded successfully: $sanPhamObj")
            sanPhamObj
        } catch (e: Exception) {
            Log.e("ProductDetail", "Error decoding SanPham: ${e.message}", e)
            null
        }
    }

    val chiTietSanPhamList = chiTietSanPhamMapJson?.let {
        val decodedJson = URLDecoder.decode(it, "UTF-8")
        Log.d("ProductDetail", "Decoded chiTietSanPhamMapJson: $decodedJson") // Log JSON sau khi decode
        try {
            val chiTietList = Json.decodeFromString<List<ChiTietSanPham>>(decodedJson)
            Log.d("ProductDetail", "ChiTietSanPhamList decoded successfully: $chiTietList")
            chiTietList
        } catch (e: Exception) {
            Log.e("ProductDetail", "Error decoding ChiTietSanPhamList: ${e.message}", e)
            null
        }
    }

    // Sử dụng ảnh đầu tiên làm ảnh chính, nếu có
    val ipAddress = "http://192.168.16.104:5000"

// Sử dụng ảnh đầu tiên làm ảnh chính, nếu có
    val selectedImage = remember {
        mutableStateOf(sanPham?.anhSP?.firstOrNull()?.let { "$ipAddress$it" } ?: "")
    }
    val imageScrollState = rememberScrollState()

    chiTietSanPhamList?.let { list ->
        println("Chi tiết sản phẩm: ${list.joinToString { it.MoTa }}")
    }

    var selectedRam by remember { mutableStateOf<String?>(null) }
    var selectedSSD by remember { mutableStateOf<String?>(null) }
    var selectedManHinh by remember { mutableStateOf<String?>(null) }
    var selectedMauSac by remember { mutableStateOf<String?>(null) }

    // Lọc các lựa chọn từ danh sách chi tiết sản phẩm
    val ramOptions = chiTietSanPhamList?.map { it.Ram }?.distinct() ?: emptyList()
    val ssdOptions = chiTietSanPhamList?.map { it.SSD }?.distinct() ?: emptyList()
    val manHinhOptions = chiTietSanPhamList?.map { it.ManHinh }?.distinct() ?: emptyList()
    val mauSacOptions = chiTietSanPhamList?.map { it.MauSac }?.distinct() ?: emptyList()

    // Hàm để lọc chi tiết sản phẩm
    val filteredProducts = chiTietSanPhamList?.filter {
        (selectedRam == null || it.Ram == selectedRam) &&
                (selectedSSD == null || it.SSD == selectedSSD) &&
                (selectedManHinh == null || it.ManHinh == selectedManHinh) &&
                (selectedMauSac == null || it.MauSac == selectedMauSac)
    }

    // Kiểm tra nếu có sản phẩm phù hợp
    val selectedProduct = filteredProducts?.firstOrNull()





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
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "favorite",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Product Image
        item {
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
            ) {
                // Hiển thị ảnh chính
                if (selectedImage.value.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = selectedImage.value),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                BorderStroke(2.dp, Color(0x809C7056)),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentScale = ContentScale.Crop
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
                                Image(
                                    painter = rememberAsyncImagePainter(model = imageUrl),
                                    contentDescription = "Image",
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .size(70.dp)
                                        .border(
                                            BorderStroke(
                                                2.dp,
                                                if (selectedImage.value == imageUrl) Color(0x809C7056) else Color(0xB3FFFFFF)
                                            ),
                                            shape = RoundedCornerShape(5.dp)
                                        ),
                                    contentScale = ContentScale.Crop
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
        // Reviews Section
        item {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, end = 20.dp, start = 20.dp, bottom = 30.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                //Tao chu nghieng va gach chan
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
                Spacer(modifier = Modifier.width(8.dp))
                AverageRatingRow(reviewData = reviewData)
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
        if (isReviewDropdownVisible) {
            items(reviewData) { review ->
                ReviewItem(review)
            }
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
                    onClick = {},
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


                    CartIconWithLoginCheck(
                        navController = navController,
                        selectedProduct = selectedProduct
                    )


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

// Hàm mở rộng để định dạng số thập phân
fun Float.format(digits: Int) = "%.${digits}f".format(this)

//@Composable
//@Preview(showBackground = true)
//private fun ProductDetailPreview() {
//    ProductDetail(navController = rememberNavController())
//}