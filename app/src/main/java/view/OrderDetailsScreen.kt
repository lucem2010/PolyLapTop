package view




import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

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

import viewmodel.CartViewModel
import viewmodel.PaymentViewModel



@Composable
fun OrderDetailsScreen(navController: NavController, chiTietSanPhamJson: String?, viewModel: PaymentViewModel) {


    val context = LocalContext.current
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
            val type = object : TypeToken<Map<String, Pair<Double, LinkedTreeMap<String, Any>>>>() {}.type
            val rawMap: Map<String, Pair<Double, LinkedTreeMap<String, Any>>> = Gson().fromJson(json, type)

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
        ApiService.SanPhamCT(idSanPhamCT = it.second._id,
            SoLuongMua = it.first.toInt())
    }
//    Log.d("CTSP", "OrderDetailsScreen: ${danhSachChiTietSanPham::class.simpleName}")
//    Log.d("PTTT", "OrderDetailsScreen: $phuongThucThanhToan")






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
            if (user != null) {
                Adress(user)
            } // Hiển thị địa chỉ
            Spacer(modifier = Modifier.height(10.dp))
            Detail(navController,chiTietSanPhamMap) // Hiển thị chi tiết
            Spacer(modifier = Modifier.height(10.dp))
//            Voucher() // Hiển thị voucher
//            Spacer(modifier = Modifier.height(10.dp))
//            /TomTat() // Hiển thị tóm tắt
//            Spacer(modifier = Modifier.height(10.dp))
            PhuongThucThanhToan(onTextPhuongThuc = {newText -> phuongThucThanhToan = newText},onTextPhuongThucPayment = {newText -> phuongThucThanhToanPayment = newText}) // Hiển thị phương thức thanh toán

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
                        text = "Tổng (${chiTietSanPhamMap.values.size} mặt hàng)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "$TongTien đ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Nút đặt hàng
                Button(
                    onClick = {
                        if(phuongThucThanhToan.trim().isEmpty()) {
                           return@Button Toast.makeText(context,"Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
                        }
                        if(phuongThucThanhToanPayment.trim().isEmpty() && phuongThucThanhToan.trim().isNotEmpty()){
                            viewModel.thanhToanOffline(
                            token = token, phuongThuc =  phuongThucThanhToan, danhSachSP =  danhSachChiTietSanPham, context =  context,
                                onSuccess = {
                                    navController.navigate(Screen.BottomNav.route){
                                    popUpTo("welcome") { inclusive = true }
                                }}
                            )
                        } else if( phuongThucThanhToanPayment.equals("Zalo") && phuongThucThanhToan.trim().isNotEmpty()){
                            viewModel.createOrderAndPay(
                                amount = TongTien.toString(),
                                context = context,
                                onPaymentSuccess = {
                                    viewModel.thanhToanOffline(
                                        token = token, phuongThuc =  phuongThucThanhToan, danhSachSP =  danhSachChiTietSanPham, context =  context,
                                        onSuccess = {
                                            navController.navigate(Screen.BottomNav.route){
                                                popUpTo("welcome") { inclusive = true }
                                            }}
                                    )
                                },
                                onPaymentError = {
                                    Toast.makeText(context, "Lỗi thanh toán: $it", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    },
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
fun Adress(user: User) {
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
                    text = "${user.HoTen}",
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
                text = "Địa chỉ nhận hàng: ${user.DiaChi}",
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
fun PhuongThucThanhToan(
    onTextPhuongThuc: (String) -> Unit,
    onTextPhuongThucPayment: (String) -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Phương thức thanh toán",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
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
            }
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
            }
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

        Spacer(modifier = Modifier.height(16.dp))

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
}

@Composable
fun PaymentOption(
    @DrawableRes icon: Int,
    text: String,
    description: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            description?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Icon(
            painter = painterResource(
                id = if (isSelected) R.drawable.ic_selected else R.drawable.ic_unselected // Icon toggle
            ),
            contentDescription = null,
            tint = if (isSelected) Color.Green else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
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