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
import androidx.compose.runtime.livedata.observeAsState
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
import model.Screen
import model.SharedPrefsManager
import viewmodel.CartViewModel
import viewmodel.UserViewModel

@Composable
fun CartIconWithLoginCheck(
    navController: NavController,
    selectedProduct: ChiTietSanPham?, // Đảm bảo `selectedProduct` là kiểu dữ liệu chính xác
    cartViewModel: CartViewModel,
    viewModel: UserViewModel
) {
    val context = LocalContext.current
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)
    val isUserLoggedIn = loggedInUser != null
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
                            if (token != null) {
                                cartViewModel.addToCart(
                                    token,
                                    selectedProduct._id,
                                    context
                                )
                            }
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
            .background(borderColor)
            .height(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart Icon",
            modifier = Modifier.size(30.dp),
            tint = iconColor
        )
    }

    // Dialog yêu cầu đăng nhập
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Vui lòng đăng nhập", color = textColor) },
            text = { Text("Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.", color = textColor) },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(Screen.Auth.route)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor
                    ),
                ) {
                    Text("Đăng nhập", color = textColor)
                }
            },
            backgroundColor = backgroundColor,
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor
                    ),
                ) {
                    Text("Đóng", color = textColor)
                }
            }
        )
    }

    // Dialog sản phẩm hết hàng
    if (showOutOfStockDialog) {
        AlertDialog(
            onDismissRequest = { showOutOfStockDialog = false },
            title = { Text("Sản phẩm hết hàng", color = textColor) },
            text = { Text("Sản phẩm bạn chọn hiện không có sẵn.", color = textColor) },
            confirmButton = {
                Button(
                    onClick = { showOutOfStockDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor
                    ),
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Dialog thêm giỏ hàng thành công
    if (showAddToCartSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showAddToCartSuccessDialog = false },
            title = { Text("Thành công", color = textColor) },
            text = { Text("Sản phẩm đã được thêm vào giỏ hàng.", color = textColor) },
            confirmButton = {
                Button(
                    onClick = { showAddToCartSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor
                    ),
                ) {
                    Text("OK", color = textColor)
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