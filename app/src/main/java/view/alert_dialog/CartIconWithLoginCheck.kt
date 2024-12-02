package view.alert_dialog


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import model.ChiTietSanPham
import model.EncryptedPrefsManager
import model.Screen
import viewmodel.CartViewModel

@Composable
fun CartIconWithLoginCheck(
    navController: NavController,
    selectedProduct: ChiTietSanPham?, // Đảm bảo `selectedProduct` là kiểu dữ liệu chính xác
    cartViewModel: CartViewModel
) {

    // Lấy Context hiện tại
    val context = LocalContext.current

    // Kiểm tra trạng thái đăng nhập
    val loginInfo = EncryptedPrefsManager.getLoginInfo(context)
    val isUserLoggedIn = loginInfo != null
    val token = loginInfo?.token ?: ""

    // State để quản lý dialog
    var showDialog by remember { mutableStateOf(false) }
    var showOutOfStockDialog by remember { mutableStateOf(false) }
    var showAddToCartSuccessDialog by remember { mutableStateOf(false) }


    // IconButton giỏ hàng
    IconButton(
        onClick = {
            try {
                when {
                    !isUserLoggedIn -> {
                        showDialog = true
                    }
                    selectedProduct == null -> {
                        showOutOfStockDialog = true
                    }
                    else -> {

                        if (selectedProduct != null) {
                            cartViewModel.addToCart(
                                token,
                                selectedProduct._id,
                                context
                            )
//                            showAddToCartSuccessDialog = true
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("CartError", "Error occurred: ${e.localizedMessage}", e)
            }
        },
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selectedProduct != null) Color(0xFFFFA500) else Color.LightGray)
            .height(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart Icon",
            modifier = Modifier.size(30.dp),
            tint = Color.White
        )
    }

    // Dialog yêu cầu đăng nhập
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Vui lòng đăng nhập") },
            text = { Text("Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.") },
            confirmButton = {
                Button(onClick = {
                    navController.navigate(Screen.Auth.route)
                    showDialog = false
                }) {
                    Text("Đăng nhập")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }

    // Dialog sản phẩm hết hàng
    if (showOutOfStockDialog) {
        AlertDialog(
            onDismissRequest = { showOutOfStockDialog = false },
            title = { Text("Sản phẩm hết hàng") },
            text = { Text("Sản phẩm bạn chọn hiện không có sẵn.") },
            confirmButton = {
                Button(onClick = { showOutOfStockDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Dialog thêm giỏ hàng thành công
    if (showAddToCartSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showAddToCartSuccessDialog = false },
            title = { Text("Thành công") },
            text = { Text("Sản phẩm đã được thêm vào giỏ hàng.") },
            confirmButton = {
                Button(onClick = { showAddToCartSuccessDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCartIconWithLoginCheck() {
//    CartIconWithLoginCheck(navController = rememberNavController(), selectedProduct = null)
//}