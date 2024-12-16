package view

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import bottomnavigation.ScreenBottomNavigation.DividerLine
import coil.compose.AsyncImage
import com.example.polylaptop.R
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import data.ApiService
import model.AppConfig
import model.ChiTietSanPham
import model.Format
import model.Screen
import model.SharedPrefsManager
import model.User
import model.toJson
import viewmodel.PaymentViewModel
import viewmodel.UserViewModel

@Composable
fun OrderDetailsScreen(
    navController: NavController,
    chiTietSanPhamJson: String?,
    viewModel: PaymentViewModel,
    viewModelUser: UserViewModel
) {
    val context = LocalContext.current
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    var phuongThucThanhToan by remember { mutableStateOf("") }
    var phuongThucThanhToanPayment by remember { mutableStateOf("") }
    val loginInfo = SharedPrefsManager.getLoginInfo(context)
    val user = loginInfo?.first
    val token = loginInfo?.second.toString()
//    Log.d("loginInfo", "OrderDetailsScreen: $token")
//    Log.d("abc", "OrderDetailsScreen: vao day ")
//     Giải mã JSON thành Map
    // Parse JSON thành Map với xử lý rõ ràng từng phần tử
    val chiTietSanPhamMap: Map<String, Pair<Double, ChiTietSanPham>> = try {
        chiTietSanPhamJson?.let { json ->
            val type =
                object : TypeToken<Map<String, Pair<Double, LinkedTreeMap<String, Any>>>>() {}.type
            val rawMap: Map<String, Pair<Double, LinkedTreeMap<String, Any>>> =
                Gson().fromJson(json, type)

            // Chuyển đổi LinkedTreeMap thành đối tượng ChiTietSanPham
            rawMap.mapValues { entry ->
                val (quantity, productMap) = entry.value
                val chiTietSanPham = Gson().fromJson(
                    Gson().toJson(productMap),
                    ChiTietSanPham::class.java
                )
//                Log.d("abc", "OrderDetailsScreen: $chiTietSanPham ")
                Pair(quantity, chiTietSanPham)
            }
        } ?: emptyMap()
    } catch (e: Exception) {
        Log.e("OrderDetailsScreen", "Error parsing JSON: ${e.message}")
        emptyMap()
    }
    val TongTien = chiTietSanPhamMap.values.sumOf { it ->
        it.first.toInt() * it.second.Gia.toInt()
    }

    val danhSachChiTietSanPham: List<ApiService.SanPhamCT> = chiTietSanPhamMap.values.map { it ->
        ApiService.SanPhamCT(
            idSanPhamCT = it.second._id,
            SoLuongMua = it.first.toInt()
        )
    }
//    Log.d("CTSP", "OrderDetailsScreen: ${danhSachChiTietSanPham::class.simpleName}")
//    Log.d("PTTT", "OrderDetailsScreen: $phuongThucThanhToan")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Thanh tiêu đề cố định ở trên cùng của màn hình
            Header(navController = navController, viewModel = viewModelUser)
            // Nội dung chính của màn hình với khả năng cuộn
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 100.dp,
                        bottom = 40.dp
                    ) // Chừa khoảng trống cho tiêu đề và nút đặt hàng
                    .background(backgroundColor)
                    .verticalScroll(rememberScrollState()), // Thêm khả năng cuộn
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                // Gọi các hàm để hiển thị nội dung
                if (user != null) {
                    Adress(user, viewModel = viewModelUser)
                } // Hiển thị địa chỉ
                Spacer(modifier = Modifier.height(20.dp))
                Detail(
                    navController,
                    chiTietSanPhamMap,
                    viewModelUser = viewModelUser
                ) // Hiển thị chi tiết
                Spacer(modifier = Modifier.height(20.dp))
//            Voucher() // Hiển thị voucher
//            Spacer(modifier = Modifier.height(10.dp))
//            /TomTat() // Hiển thị tóm tắt
//            Spacer(modifier = Modifier.height(10.dp))
                PhuongThucThanhToan(
                    onTextPhuongThuc = { newText -> phuongThucThanhToan = newText },
                    onTextPhuongThucPayment = { newText ->
                        phuongThucThanhToanPayment = newText
                    },
                    viewModelUser = viewModelUser
                ) // Hiển thị phương thức thanh toán
                // Spacer để tạo khoảng trống cuối để không bị Box đè lên
            }
            // Box cố định ở dưới cùng của màn hình
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(backgroundColor)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Tạo khoảng cách giữa các phần tử
                ) {
                    // Hiển thị tổng tiền và số lượng mặt hàng
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tổng (${chiTietSanPhamMap.values.size} mặt hàng)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = "$TongTien đ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor

                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    // Nút đặt hàng
                    Button(
                        onClick = {
                            if (phuongThucThanhToan.trim().isEmpty()) {
                                return@Button Toast.makeText(
                                    context,
                                    "Vui lòng chọn phương thức thanh toán",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (phuongThucThanhToanPayment.trim()
                                    .isEmpty() && phuongThucThanhToan.trim().isNotEmpty()
                            ) {
                                viewModel.thanhToanOffline(
                                    token = token,
                                    phuongThuc = phuongThucThanhToan,
                                    danhSachSP = danhSachChiTietSanPham,
                                    context = context,
                                    onSuccess = {
                                        navController.navigate(Screen.BottomNav.route) {
                                            popUpTo("welcome") { inclusive = true }
                                        }
                                    }
                                )
                            } else if (phuongThucThanhToanPayment.equals("Zalo") && phuongThucThanhToan.trim()
                                    .isNotEmpty()
                            ) {
                                viewModel.createOrderAndPay(
                                    amount = TongTien.toString(),
                                    context = context,
                                    onPaymentSuccess = {
                                        viewModel.thanhToanOffline(
                                            token = token,
                                            phuongThuc = phuongThucThanhToan,
                                            danhSachSP = danhSachChiTietSanPham,
                                            context = context,
                                            onSuccess = {
                                                navController.navigate(Screen.BottomNav.route) {
                                                    popUpTo("welcome") { inclusive = true }
                                                }
                                            }
                                        )
                                    },
                                    onPaymentError = {
                                        Toast.makeText(
                                            context,
                                            "Lỗi thanh toán: $it",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = borderColor
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = "Đặt hàng", color = textColor)
                    }
                }
            }
        }
    }
}

@Composable
fun Header(navController: NavController, viewModel: UserViewModel) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 14.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(borderColor)
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.left),
                contentDescription = "Back",
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Chi tiết đơn hàng",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = "Thông tin của bạn sẽ được bảo mật",
                fontSize = 18.sp,
                color = Color.Green
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Adress(user: User, viewModel: UserViewModel) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    DividerLine()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor), // Màu nền cho tiêu đề
        contentAlignment = Alignment.Center // Canh giữa nội dung Text trong Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ), // Khoảng cách từ cạnh trái
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_location_on_24),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(30.dp),
                    tint = iconColor
                )
                Text(
                    text = "${user.HoTen}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(10.dp)) // Khoảng cách giữa hai Text
                Text(
                    text = "(+84)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = " 91*****19",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.weight(1f) // Đẩy các phần tử phía sau về bên phải
                )
            }
            Text(
                text = "Địa chỉ nhận hàng: ${user.DiaChi}",
                fontSize = 17.sp,
                color = textColor,
                modifier = Modifier
                    .padding(start = 26.dp),

                )
        }
    }
    DividerLine()
}

@Composable
fun Detail(
    navController: NavController,
    chiTietSanPhamMap: Map<String, Pair<Double, ChiTietSanPham>>,
    viewModelUser: UserViewModel
) {
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    // Tạo danh sách các chi tiết sản phẩm từ Map
    val danhSachChiTietSanPham = chiTietSanPhamMap.values.map { Pair(it.first, it.second) }
    // Hiển thị danh sách chi tiết sản phẩm
    DividerLine()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.TopStart
    ) {
        LazyColumn {
            items(danhSachChiTietSanPham) { (soLuong, chiTietSanPham) ->
                ChiTietSanPhamItem(
                    soLuong,
                    chiTietSanPham,
                    navController,
                    viewModelUser = viewModelUser
                )
            }
        }
    }
    DividerLine()
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
        Column(
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
                    .padding(bottom = 15.dp, top = 10.dp),
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
                )
                Text(
                    text = "$",
                    fontSize = 17.sp,
                    color = Color.Black,
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
fun PhuongThucThanhToan(
    onTextPhuongThuc: (String) -> Unit,
    onTextPhuongThucPayment: (String) -> Unit,
    viewModelUser: UserViewModel
) {
    var selectedPaymentMethod by remember { mutableStateOf("") }
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    DividerLine()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Text(
            text = "Phương thức thanh toán",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Thanh toán khi nhận hàng
        PaymentOption(
            icon = R.drawable.ic_cod, // Thay bằng icon COD
            text = "Thanh toán khi nhận hàng",
            isSelected = selectedPaymentMethod == "COD",
            onClick = {
                selectedPaymentMethod = "COD"
                onTextPhuongThuc("Nhận hàng thanh toán")
                onTextPhuongThucPayment("")
            },
            viewModelUser = viewModelUser
        )
        // ZaloPay
        PaymentOption(
            icon = R.drawable.ic_zalopay, // Thay bằng icon MoMo
            text = "Thanh toán bằng Zalo Pay",
            isSelected = selectedPaymentMethod == "Zalo",
            onClick = {
                selectedPaymentMethod = "Zalo"
                onTextPhuongThuc("Thanh toán payment")
                onTextPhuongThucPayment("Zalo")
            },
            viewModelUser = viewModelUser
        )
        //
//        PaymentOption(
//            icon = R.drawable.ic_zalopay, // Thay bằng icon ZaloPay
//            text = "ZaloPay",
//            description = "Liên kết tài khoản ZaloPay của bạn để thanh toán dễ dàng.",
//            isSelected = selectedPaymentMethod == "ZaloPay",
//            onClick = {
//                selectedPaymentMethod = "ZaloPay"
//                onTextPhuongThuc("Thanh toán payment")
//                onTextPhuongThucPayment("Zalo")
//            }
//        )
        // Xem thêm tùy chọn
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { /* Xử lý khi mở tất cả tùy chọn */ },
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "Xem tất cả tùy chọn",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Blue
//            )
//            Icon(
//                painter = painterResource(id = R.drawable.chevron), // Thay icon phù hợp
//                contentDescription = "Chevron",
//                tint = Color.Gray,
//                modifier = Modifier.size(20.dp)
//            )
//        }
    }
    DividerLine()
}

@Composable
fun PaymentOption(
    @DrawableRes icon: Int,
    text: String,
    description: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
    viewModelUser: UserViewModel
) {
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 8.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor)
            description?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Icon(
            painter = painterResource(
                id = if (isSelected) R.drawable.ic_selected else R.drawable.ic_unselected // Icon toggle
            ),
            contentDescription = null,
            tint = if (isSelected) Color.Green else Color.White,
            modifier = Modifier
                .size(20.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                .clip(
                    RoundedCornerShape(5.dp)
                )
        )
    }
}

@Composable
fun ChiTietSanPhamItem(
    soLuong: Double, product: ChiTietSanPham, navController: NavController,
    viewModelUser: UserViewModel
) {
    val formattedDiscountedPrice = Format().formatPrice(product.Gia)
    val ipAddress = AppConfig.ipAddress
    val imageUrl = product.idSanPham.anhSP?.firstOrNull()?.let { "$ipAddress$it" }
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    DividerLine()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        Text(
            text = "Thông tin sản phẩm",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    .width(100.dp) // Đặt chiều rộng nhỏ hơn
                    .height(100.dp) // Đặt chiều cao nhỏ hơn
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
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.weight(1f) // Chiếm không gian còn lại
                    )

                    Text(
                        text = "x${soLuong.toInt()}", // Số lượng
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = formattedDiscountedPrice, // Giá sản phẩm
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "${product.MoTa}", // Mo ta san pham
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

//// Hàm Preview cho CartScreen
@Preview(showBackground = true)
@Composable
fun OrderDetailsScreenPreview() {
    OrderDetailsScreen(
        viewModel = PaymentViewModel(),
        viewModelUser = UserViewModel(),
        navController = rememberNavController(),
        chiTietSanPhamJson = null
    )
}