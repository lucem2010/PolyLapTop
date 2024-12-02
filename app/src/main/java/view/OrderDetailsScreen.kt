package view



import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.polylaptop.R
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import model.AppConfig
import model.ChiTietSanPham
import model.Format
import model.HangSP
import model.SanPham
import model.Screen
import model.toJson
import java.net.URLDecoder
import java.text.NumberFormat
import java.util.Locale


@Composable
fun OrderDetailsScreen(navController: NavController, chiTietSanPhamJson: String?) {
//     Giải mã JSON thành Map
    // Parse JSON thành Map với xử lý rõ ràng từng phần tử
    val chiTietSanPhamMap: Map<String, Pair<Double, ChiTietSanPham>> = try {
        chiTietSanPhamJson?.let { json ->
            val type = object : TypeToken<Map<String, Pair<Double, LinkedTreeMap<String, Any>>>>() {}.type
            val rawMap: Map<String, Pair<Double, LinkedTreeMap<String, Any>>> = Gson().fromJson(json, type)

            // Chuyển đổi LinkedTreeMap thành đối tượng ChiTietSanPham
            rawMap.mapValues { entry ->
                val (quantity, productMap) = entry.value
                val chiTietSanPham = Gson().fromJson(
                    Gson().toJson(productMap),
                    ChiTietSanPham::class.java
                )
                Pair(quantity, chiTietSanPham)
            }
        } ?: emptyMap()
    } catch (e: Exception) {
        Log.e("OrderDetailsScreen", "Error parsing JSON: ${e.message}")
        emptyMap()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff))
    ) {
        // Thanh tiêu đề cố định ở trên cùng của màn hình
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White), // Màu nền cho tiêu đề
            contentAlignment = Alignment.Center // Canh giữa nội dung Text trong Box
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp), // Khoảng cách từ cạnh trái
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Chi tiết đơn hàng",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Thông tin của bạn sẽ được bảo mật",
                    fontSize = 13.sp,
                    color = Color.Green
                )
            }
        }

        // Nội dung chính của màn hình với khả năng cuộn
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 60.dp,
                    bottom = 70.dp
                ) // Chừa khoảng trống cho tiêu đề và nút đặt hàng
                .background(Color(0xFFEFEFEF))
                .verticalScroll(rememberScrollState()), // Thêm khả năng cuộn
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            // Gọi các hàm để hiển thị nội dung
            Adress() // Hiển thị địa chỉ
            Spacer(modifier = Modifier.height(10.dp))
            Detail(navController,chiTietSanPhamMap) // Hiển thị chi tiết
            Spacer(modifier = Modifier.height(10.dp))
            Voucher() // Hiển thị voucher
            Spacer(modifier = Modifier.height(10.dp))
            TomTat() // Hiển thị tóm tắt
            Spacer(modifier = Modifier.height(10.dp))
            PhuongThucThanhToan() // Hiển thị phương thức thanh toán

            // Spacer để tạo khoảng trống cuối để không bị Box đè lên
            Spacer(modifier = Modifier.height(70.dp))
        }

        // Box cố định ở dưới cùng của màn hình
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Tạo khoảng cách giữa các phần tử
            ) {
                // Hiển thị tổng tiền và số lượng mặt hàng
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tổng (1 mặt hàng)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "179.000đ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Nút đặt hàng
                Button(
                    onClick = { /* Xử lý đặt hàng */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF8774A)
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Đặt hàng", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun Adress() {
    Spacer(modifier = Modifier.height(5.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .background(Color.White), // Màu nền cho tiêu đề
        contentAlignment = Alignment.Center // Canh giữa nội dung Text trong Box
    ) {

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp), // Khoảng cách từ cạnh trái
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc

            ) {
                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
//                            navController.popBackStack()
                        }
                )
                Text(
                    text = "Trần Ngọc Hải",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(5.dp)) // Khoảng cách giữa hai Text
                Text(
                    text = "(+84)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "91*****19",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f) // Đẩy các phần tử phía sau về bên phải

                )
                Image(
                    painter = painterResource(id = R.drawable.chevron),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
//                            navController.popBackStack()
                        }
                )
            }

            Text(
                text = "Địa chỉ nhận hàng",
                fontSize = 17.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 24.dp),

                )
        }
    }



}


@Composable
fun Detail(
    navController: NavController,
    chiTietSanPhamMap: Map<String, Pair<Double, ChiTietSanPham>>
) {
    // Tạo danh sách các chi tiết sản phẩm từ Map
    val danhSachChiTietSanPham = chiTietSanPhamMap.values.map { Pair(it.first, it.second) }

    // Hiển thị danh sách chi tiết sản phẩm
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        LazyColumn {
            items(danhSachChiTietSanPham) { (soLuong, chiTietSanPham) ->
                ChiTietSanPhamItem(soLuong, chiTietSanPham, navController)
            }
        }
    }
}


@Composable
fun Voucher() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.White), // Màu nền cho tiêu đề
        contentAlignment = Alignment.Center // Canh giữa nội dung Text trong Box
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp), // Khoảng cách từ cạnh trái
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // Canh giữa theo chiều dọc
                modifier = Modifier
                    .padding(bottom = 15.dp, top = 10.dp),


                ) {
                Image(
                    painter = painterResource(id = R.drawable.voucher),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
//                            navController.popBackStack()
                        }
                )

                Text(
                    text = "Chiết khấu voucher",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.chevron),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
//                            navController.popBackStack()
                        }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc

            ) {
                Text(
                    text = "Voucher giảm giá ",
                    fontSize = 17.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "Đã quy đổi",
                    fontSize = 17.sp,
                    color = Color.Red,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
                Text(
                    text = "1",
                    fontSize = 17.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(start = 2.dp),

                    )
            }
        }
    }

}
@Composable
fun TomTat() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(Color.White), // Màu nền cho tiêu đề
        contentAlignment = Alignment.Center // Canh giữa nội dung Text trong Box
    ) {

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp), // Khoảng cách từ cạnh trái
        ) {

            Text(
                text = "Tóm tắt đơn hàng",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 15.dp,top = 10.dp),

                )
            Row(
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc

            ) {
                Text(
                    text = "Tổng phụ",
                    fontSize = 17.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "500",
                    fontSize = 17.sp,
                    color = Color.Black,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
                Text(
                    text = "$",
                    fontSize = 17.sp,
                    color = Color.Black,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc

            ) {
                Text(
                    text = "Vận chuyển",
                    fontSize = 17.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "-",
                    fontSize = 17.sp,
                    color = Color.Red,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
                Text(
                    text = "18",
                    fontSize = 17.sp,
                    color = Color.Red,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
                Text(
                    text = "$",
                    fontSize = 17.sp,
                    color = Color.Red,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc

            ) {
                Text(
                    text = "Chiết khấu voucher",
                    fontSize = 17.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "50",
                    fontSize = 17.sp,
                    color = Color.Black,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
                Text(
                    text = "$",
                    fontSize = 17.sp,
                    color = Color.Black,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc

            ) {
                Text(
                    text = "Tổng",
                    fontSize = 17.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "468",
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,

//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
                Text(
                    text = "$",
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .padding(start = 24.dp),

                )
            }



        }
    }

}
@Composable
fun PhuongThucThanhToan() {
    var isCodSelected by remember { mutableStateOf(false) }
    var isMomoSelected by remember { mutableStateOf(false) }
    var isZaloSelected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Phương thức thanh toán",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Phương thức COD
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Thanh toán khi nhận hàng",
                    fontSize = 16.sp,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(
                        id = if (isCodSelected) R.drawable.button_red else R.drawable.button
                    ),
                    contentDescription = "COD Button",
                    tint = if (isCodSelected) Color.Red else Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            isCodSelected = !isCodSelected
                            isMomoSelected = false
                            isZaloSelected = false
                        }
                )
            }

            Divider(color = Color.Gray, thickness = 0.5.dp)

            // Phương thức Momo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Ví điện tử MoMo(****7619)",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(
                        id = if (isMomoSelected) R.drawable.button_red else R.drawable.button
                    ),
                    contentDescription = "MoMo Button",
                    tint = if (isMomoSelected) Color.Red else Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            isMomoSelected = !isMomoSelected
                            isCodSelected = false
                            isZaloSelected = false
                        }
                )
            }

            Divider(color = Color.Black, thickness = 0.5.dp)

            // Phương thức ZaloPay
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ZaloPay",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Liên kết",
                            fontSize = 14.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.chevron),
                            contentDescription = "Chevron",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Liên kết tài khoản ZaloPay của bạn với TikTok Shop và thử tính năng thanh toán không cần mật khẩu",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = Color.Gray, thickness = 0.5.dp)

            // Xem tất cả tùy chọn
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Xử lý mở rộng tất cả tùy chọn */ }
                    .padding(vertical = 12.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Xem tất cả tùy chọn",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painterResource(id = R.drawable.chevron),
                    contentDescription = "Chevron",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ChiTietSanPhamItem(soLuong: Double, product: ChiTietSanPham,navController: NavController) {
    val formattedDiscountedPrice = Format().formatPrice(product.Gia)
    val ipAddress = AppConfig.ipAddress
    val imageUrl = product.idSanPham.anhSP?.firstOrNull()?.let { "$ipAddress$it" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { // Xử lý khi click vào item
                val chiTietSanPhamJson = Uri.encode(toJson(product ?: emptyMap<String, Any>()))
                navController.navigate(Screen.ProductDetail.route + "/${chiTietSanPhamJson}")
            },

        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = product.idSanPham.tenSP,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(35.dp) // Đặt chiều rộng nhỏ hơn
                .height(35.dp) // Đặt chiều cao nhỏ hơn
                .clip(RoundedCornerShape(6.dp)) // Giữ bo góc hợp lý với kích thước nhỏ
        )




        Spacer(modifier = Modifier.width(16.dp))

        // Hiển thị thông tin sản phẩm bên phải
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = product.idSanPham.tenSP, // Tên sản phẩm
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f) // Chiếm không gian còn lại
                )
                Text(
                    text = "x$soLuong", // Số lượng
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = formattedDiscountedPrice, // Giá sản phẩm
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}




//// Hàm Preview cho CartScreen
//@Preview(showBackground = true)


//@Composable
//fun OrderDetailsScreenPreview() {
//    val navController = rememberNavController() // Khởi tạo NavController giả lập
//    OrderDetailsScreen(navController = navController)
//}