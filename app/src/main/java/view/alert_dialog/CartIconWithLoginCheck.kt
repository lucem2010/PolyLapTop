package view.alert_dialog


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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import model.EncryptedPrefsManager
import model.Screen

@Composable
fun CartIconWithLoginCheck(
    navController: NavController,
    selectedProduct: Any? // Giả sử selectedProduct là đối tượng bạn kiểm tra sản phẩm
) {
    // Lấy Context hiện tại trong Composable
    val context = LocalContext.current
    // Kiểm tra trạng thái đăng nhập
    val isUserLoggedIn = EncryptedPrefsManager.isUserLoggedIn(context)

    // State để quản lý việc hiển thị dialog
    var showDialog by remember { mutableStateOf(false) }
    // State để quản lý thông báo hết hàng
    var showOutOfStockDialog by remember { mutableStateOf(false) }

    // Icon "Giỏ hàng"
    IconButton(
        onClick = {
            if (!isUserLoggedIn) {
                // Nếu chưa đăng nhập, hiển thị thông báo yêu cầu đăng nhập
                showDialog = true
            } else {
                // Nếu đã đăng nhập, kiểm tra selectedProduct
                if (selectedProduct == null) {
                    // Nếu selectedProduct là null, hiển thị thông báo hết hàng
                    showOutOfStockDialog = true
                } else {
                    // Nếu có sản phẩm, tiếp tục xử lý hành động giỏ hàng
                }
            }
        },
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selectedProduct != null) Color(0xFFFFA500) else Color.LightGray)
            .height(50.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "cart",
            modifier = Modifier.size(30.dp)
        )
    }

    // Hiển thị dialog yêu cầu đăng nhập nếu chưa đăng nhập
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Vui lòng đăng nhập") },
            text = { Text("Bạn cần đăng nhập để tiếp tục.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Điều hướng đến màn hình đăng nhập
                        navController.navigate(Screen.Auth.route)
                        showDialog = false
                    }
                ) {
                    Text("Đăng nhập")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Đóng")
                }
            }
        )
    }

    // Hiển thị thông báo sản phẩm hết hàng
    if (showOutOfStockDialog) {
        AlertDialog(
            onDismissRequest = { showOutOfStockDialog = false },
            title = { Text("Sản phẩm hết hàng") },
            text = { Text("Sản phẩm bạn chọn hiện tại đã hết hàng.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Đóng thông báo hết hàng
                        showOutOfStockDialog = false
                    }
                ) {
                    Text("Đóng")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartIconWithLoginCheck() {
    CartIconWithLoginCheck(navController = rememberNavController(), selectedProduct = null)
}
