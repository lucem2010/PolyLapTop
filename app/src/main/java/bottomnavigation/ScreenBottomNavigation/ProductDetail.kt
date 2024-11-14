package bottomnavigation.ScreenBottomNavigation

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
import com.example.polylaptop.R
import model.Review
import model.imagesItem


@Composable
fun ProductDetail(   navController: NavController,
                     sanPhamJson: String?,
                     chiTietSanPhamMapJson: String?) {
    var isFavoriteVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState() // State cho verticalScroll
    val imageScrollState = rememberScrollState() // State cho horizontalScroll
    val selectedImage = remember { mutableStateOf(imagesItem.first().pic) }
    var isReviewDropdownVisible by remember { mutableStateOf(false) }

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
                Image(
                    painter = painterResource(id = selectedImage.value),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            BorderStroke(
                                2.dp,
                                Color(0x809C7056)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Cuộn ngang ảnh bổ sung
                Row(
                    modifier = Modifier
                        .horizontalScroll(imageScrollState) // Horizontal scroll cho ảnh
                ) {
                    imagesItem.forEachIndexed { index, image ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                                .clickable {
                                    // Handle item click
                                    selectedImage.value = image.pic
                                }
                                .clip(RoundedCornerShape(5.dp)),
                        ) {
                            Image(
                                painter = painterResource(id = image.pic),
                                contentDescription = "Image ${image.id}",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .size(70.dp)
                                    .border(
                                        BorderStroke(
                                            2.dp,
                                            if (selectedImage.value == image.pic) Color(0x809C7056) else Color(
                                                0xB3FFFFFF
                                            )
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
                    text = "14.000.000 VND",
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
                DropdownOption(
                    "RAM",
                    listOf("4GB DDR4", "8GB DDR4", "16GB DDR4", "32GB DDR5", "64GB DDR5")
                )
                Spacer(modifier = Modifier.height(5.dp))
                DropdownOption(
                    "Màn hình",
                    listOf("13.3 inch FHD", "14 inch 2K", "15.6 inch FHD", "17.3 inch 4K")
                )
                Spacer(modifier = Modifier.height(5.dp))
                DropdownOption(
                    "Card đồ họa",
                    listOf(
                        "Intel UHD",
                        "Intel Iris Xe",
                        "NVIDIA GTX 1650",
                        "NVIDIA RTX 3060",
                        "AMD Radeon RX"
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                DropdownOption("Ổ cứng", listOf("256GB SSD", "512GB SSD", "1TB SSD", "2TB SSD"))
                Spacer(modifier = Modifier.height(5.dp))
                DropdownOption("Màu sắc", listOf("Đen", "Bạc", "Xám", "Trắng", "Xanh lam", "Đỏ"))
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
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0x809C7056),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(
                        "Mua ngay",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp)) // Clip để tạo bo góc đều cho IconButton
                        .background(Color.LightGray) // Màu nền cho IconButton
                        .height(50.dp)// Padding cho IconButton để tạo không gian cho biểu tượng
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "cart",
                        modifier = Modifier
                            .size(30.dp) // Kích thước biểu tượng nhỏ để phù hợp với không gian
                    )
                }
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
fun DropdownOption(label: String, options: List<String>) {
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