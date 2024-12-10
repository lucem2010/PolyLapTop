package bottomnavigation.ScreenBottomNavigation


import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import bottomnavigation.ScreenBottomNavigation.Order.CanceledOrderScreen
import bottomnavigation.ScreenBottomNavigation.Order.DeliveredOrdersScreen
import bottomnavigation.ScreenBottomNavigation.Order.PendingOrdersScreen
import bottomnavigation.ScreenBottomNavigation.Order.ShippingOrdersScreen
import model.SharedPrefsManager
import viewmodel.DonHangViewModel
import viewmodel.UserViewModel

@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: UserViewModel,
    donHangViewModel: DonHangViewModel
) {
    val context = LocalContext.current
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)

    Log.d("DonHangViewModel", "Token: $token")

    LaunchedEffect(Unit) {
        if (token != null) {
            donHangViewModel.getDonHang(token)
        }
    }

    val donHangList by donHangViewModel.donHangLiveData.observeAsState(emptyList())
    // Log donHangList khi nó thay đổi
    LaunchedEffect(donHangList) {
        if (donHangList.isNotEmpty()) {
            Log.d("OrderScreen", "DonHang List: ${donHangList.joinToString(", ") { it.toString() }}")
        } else {
            Log.d("OrderScreen", "DonHang List is empty")
        }
    }

    val tabs = listOf("Chờ xác nhận", "Đang vận chuyển", "Đã giao","Đã hủy")
    var selectedTab by remember { mutableStateOf(0) }

    // Trạng thái xác định xem có hiển thị danh mục hay không
    var showCategories by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Chờ duyệt") } // Quản lý selectedCategory nội bộ
    Column {
        // Thanh cuộn ngang cho danh mục
        Row(
            modifier = Modifier
                .height(50.dp)
                .padding(start = 10.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            val menuItems = tabs // Các tab chính
            menuItems.forEachIndexed { index, item ->
                TextButton(
                    onClick = {
                        selectedTab = index
                        selectedCategory = item // Cập nhật danh mục
                    }
                ) {
                    Text(
                        text = item,
                        fontSize = 15.sp,
                        color = if (index == selectedTab) Color.Black else Color.Gray,
                        fontWeight = if (index == selectedTab) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }


        // Nội dung theo tab
        when (selectedTab) {
            0 -> PendingOrdersScreen(
                donHangList = donHangList.filter { it.TrangThai == "Chờ duyệt" }
            )
            1 -> ShippingOrdersScreen(
                donHangList = donHangList.filter { it.TrangThai == "Đang vận chuyển"}
            )
            2 -> DeliveredOrdersScreen(
                donHangList = donHangList.filter { it.TrangThai == "Thành công" }
            )
            3 -> CanceledOrderScreen(
                donHangList = donHangList.filter { it.TrangThai ==  "Hủy"  }
            )
        }


        // Danh mục phụ nếu `showCategories` được bật
        if (showCategories) {
            Text(
                text = "Danh mục giáo dục hiển thị ở đây!",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun OrderScreenPreview() {
    OrderScreen(
        navController = rememberNavController(),
        viewModel = UserViewModel(),
        donHangViewModel = DonHangViewModel()
    )
}