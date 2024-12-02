package bottomnavigation.ScreenBottomNavigation

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.polylaptop.R
import com.google.gson.Gson
import model.AppConfig
import model.ChiTietSanPham
import model.EncryptedPrefsManager
import model.Format
import model.GioHang
import model.SanPham
import model.Screen
import model.toJson
import viewmodel.CartViewModel
import java.net.URLEncoder

@Composable
fun CartScreen( bottomNavController: NavController,
                mainNavController: NavController,) {
    val context = LocalContext.current
    val loginInfo = EncryptedPrefsManager.getLoginInfo(context)
    val ipAddress =  AppConfig.ipAddress

    val token = loginInfo.token

    // Khởi tạo ViewModel
    val cartViewModel: CartViewModel = viewModel()

    // Lấy danh sách giỏ hàng từ ViewModel
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    var selectedItems by remember { mutableStateOf<Map<String, Pair<Double, ChiTietSanPham>>>(emptyMap()) }


    // Gọi hàm fetchCartItems khi màn hình được khởi tạo
    LaunchedEffect(token) {
        token?.let {
            cartViewModel.fetchCartItems(it)
        } ?: run {
            // Xử lý khi token là null, ví dụ: hiển thị thông báo lỗi hoặc quay lại màn hình đăng nhập

        }
    }

    val totalPrice = selectedItems.values.sumOf { (quantity, chiTiet) ->
        (chiTiet.Gia * quantity).toLong() // Tính giá cho mỗi sản phẩm và ép sang Long
    }

    val formatter= Format();

    // Giao diện cho màn hình giỏ hàng
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Thanh tiêu đề
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GIỎ HÀNG",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            // Hiển thị giỏ hàng

            // Lưới hiển thị sản phẩm
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 25.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                items(cartItems) { item ->
//                         Hiển thị sản phẩm trong giỏ hàng

                    if (token != null) {
                        ItemGrid(
                            cartViewModel,
                            context,
                            ipAddress,
                            token,
                            item.idChiTietSP.idSanPham,
                            item.idChiTietSP,
                            item,
                            formatter,
                            onItemCheckedChange = { item, quantity ->
                                selectedItems = if (selectedItems.containsKey(item._id)) {
                                    // Nếu sản phẩm đã có, xóa nó khỏi danh sách
                                    selectedItems.filterNot { it.key == item._id }
                                } else {
                                    // Nếu sản phẩm chưa có, thêm vào danh sách
                                    selectedItems + (item._id to Pair(quantity.toDouble(), item))
                                }
                            },
                            onIncreaseQuantity = { item: ChiTietSanPham, quantity: Int ->
                                selectedItems = selectedItems.toMutableMap().apply {
                                    if (containsKey(item._id)) {
                                        // Cập nhật số lượng nếu sản phẩm đã tồn tại
                                        this[item._id] = this[item._id]?.let { Pair(quantity.toDouble(), it.second) }
                                            ?: Pair(quantity.toDouble(), item)
                                    }
                                }
                            },
                            onClick = {
                                val chiTietSanPhamJson = Uri.encode(toJson(item.idChiTietSP ?: emptyMap<String, Any>()))
                                Log.d("ProductDetail", "Encoded chiTietSanPhamJson: $chiTietSanPhamJson")
                                mainNavController.navigate(
                                    Screen.ProductDetail.route + "/${chiTietSanPhamJson}"
                                )
                            }
                        )
                    }
                }
            }
        }


        // Box cố định ở dưới cùng của màn hình
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFFEFEFEF))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng: ${formatter.formatPrice(totalPrice)} ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Button(
                    onClick = {
                        // Log trước khi thực hiện các bước
                        Log.d("ButtonClick", "Button clicked. Checking selectedItems.")

                        // Nếu selectedItems không rỗng, chuyển đổi và điều hướng
                        if (selectedItems.isNotEmpty()) {
                            Log.d("ButtonClick", "Selected items are not empty. Proceeding to create JSON.")

                            try {
                                // Chuyển Map selectedItems thành JSON
                                val jsonMap = Gson().toJson(selectedItems) // Convert Map to JSON
                                val encodedJson = URLEncoder.encode(jsonMap, "UTF-8") // Mã hóa chuỗi JSON
                                Log.d("ButtonClick", "Navigating with JSON: $encodedJson")

                                // Điều hướng đến OrderDetailsScreen với dữ liệu JSON
                                mainNavController.navigate(Screen.OrderDetailsScreen.route + "/$encodedJson")
                            } catch (e: Exception) {
                                Log.e("ButtonClick", "Error encoding JSON or navigating: ${e.message}")
                            }
                        } else {
                            Log.d("ButtonClick", "Selected items are empty.")
                        }
                    } ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF8774A)
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Thanh toán", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ItemGrid(
    cartViewModel: CartViewModel,
    conText: Context,
    ipAddress: String,
    token: String,
    sanPham: SanPham,
    chiTietSanPham: ChiTietSanPham,
    cart: GioHang,
    formatter:Format,
    onItemCheckedChange: (ChiTietSanPham, Int) -> Unit, // Callback
    onIncreaseQuantity : (ChiTietSanPham, Int) -> Unit, // Callback

    onClick: () -> Unit = {}
) {
    var soLuong by remember { mutableStateOf(1) }
    var isChecked by remember { mutableStateOf(false) } // State for the checkbox
    var showDialog by remember { mutableStateOf(false) }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận") },
            text = { Text("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?") },
            confirmButton = {
                Button(
                    onClick = {
                        cartViewModel.removeFromCart(token, cart._id, conText)
                        showDialog = false
                    }
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(140.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "$ipAddress${sanPham.anhSP?.firstOrNull()}",
                contentDescription = sanPham.tenSP,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = sanPham.tenSP,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(2f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Xóa",
                        modifier = Modifier.clickable { showDialog = true }
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Màu: ${chiTietSanPham.MauSac}", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if (soLuong > 1) {
                                soLuong--
                                if (isChecked) {
                                    onIncreaseQuantity(chiTietSanPham, soLuong)
                                }
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF8774A)),
                        modifier = Modifier
                            .size(25.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.minus),
                            contentDescription = "Giảm",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = soLuong.toString(),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            if (soLuong < chiTietSanPham.SoLuong) {
                                soLuong++
                                if(isChecked){
                                    onIncreaseQuantity(chiTietSanPham, soLuong)
                                }

                            } else {
                                Toast.makeText(
                                    conText,
                                    "Số lượng vượt quá số lượng có sẵn!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF8774A)),
                        modifier = Modifier
                            .size(25.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Tăng",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = formatter.formatPrice(chiTietSanPham.Gia),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            isChecked = checked
                            // Chuyển đối tượng chiTietSanPham và soLuong lên component cha
                            onItemCheckedChange(chiTietSanPham, soLuong)
                        }
                    )
                    Text(text = "Chọn sản phẩm", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}



//// Hàm Preview cho CartScreen
//@Preview(showBackground = true)
//@Composable
//fun PreviewCartScreen() {
//    val navController = rememberNavController() // NavController giả lập
//    CartScreen(navController = navController)
//}