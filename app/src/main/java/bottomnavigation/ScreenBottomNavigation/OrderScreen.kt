package bottomnavigation.ScreenBottomNavigation


import DonHang
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.lightColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun OrderScreen(
    bottomNavController: NavController,
    mainNavController: NavController,
    donHangViewModel: DonHangViewModel,
    viewModel: UserViewModel
) {
    val context = LocalContext.current
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)
    val donHangList by donHangViewModel.donHangLiveData.observeAsState(emptyList())
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val backgroundColorButton = if (isDarkTheme) Color.Gray else Color(0xFFF8774A)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    Log.d("DonHangViewModel", "Token: $token")
    // LaunchedEffect sẽ được gọi lại khi donHangList thay đổi
    LaunchedEffect(donHangList) {
        if (token != null) {
            donHangViewModel.getDonHang(token)
        }
    }
    val tabs = listOf("Chờ xác nhận", "Đang vận chuyển", "Đã giao", "Đã hủy")
    var selectedTab by remember { mutableStateOf(0) }
    // Trạng thái xác định xem có hiển thị danh mục hay không
    var showCategories by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Chờ duyệt") } // Quản lý selectedCategory nội bộ
    // State để quản lý tìm kiếm
    var searchQuery by remember { mutableStateOf("") }
    var filteredList by remember { mutableStateOf(donHangList) }
    // Lọc danh sách dựa trên tìm kiếm
    LaunchedEffect(searchQuery, selectedTab, donHangList) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        filteredList = if (searchQuery.isNotEmpty()) {
            donHangList.filter { donHang ->
                try {
                    val orderDate = dateFormat.format(donHang.NgayDatHang)
                    orderDate.contains(searchQuery, ignoreCase = true) &&
                            donHang.TrangThai == tabs[selectedTab]
                } catch (e: Exception) {
                    false
                }
            }
        } else {
            donHangList.filter { it.TrangThai == tabs[selectedTab] }
        }
    }
    val calendar = Calendar.getInstance()
    MaterialTheme(
        colors = if (isDarkTheme) darkColors() else lightColors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
        ) {
            // Thanh cuộn ngang cho danh mục
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .padding(start = 10.dp)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                ) {
                val menuItems = tabs // Các tab chính
                menuItems.forEachIndexed { index, item ->
                    // Sử dụng Surface hoặc Card để tạo hiệu ứng elevation
                    TextButton(
                        onClick = {
                            selectedTab = index
                            selectedCategory = item // Cập nhật danh mục
                        },
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .drawBehind {
                                if (index == selectedTab) {
                                    val strokeWidth = 2.dp.toPx() // Độ dày của đường viền
                                    drawLine(
                                        color = Color.LightGray,
                                        start = Offset(0f, size.height - strokeWidth / 2),
                                        end = Offset(size.width, size.height - strokeWidth / 2),
                                        strokeWidth = strokeWidth
                                    )
                                }
                            }
                    ) {
                        Text(
                            text = item,
                            fontSize = 15.sp,
                            color = if (index == selectedTab) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSurface,
                            fontWeight = if (index == selectedTab) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            // Ô tìm kiếm theo ngày
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it }, // Cập nhật giá trị tìm kiếm
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Tìm kiếm theo ngày (dd/MM/yyyy)", color = textColor) },
                leadingIcon = {
                    IconButton(onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                searchQuery =
                                    String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar Icon"
                        )
                    }
                },
                singleLine = true,
                textStyle = TextStyle(color = textColor) // Thiết lập màu văn bản khi nhập vào
            )
            // Nội dung theo tab
                when (selectedTab) {
                    0 -> PendingOrdersScreen(
                        searchQuery,
                        donHangList = donHangList.filter { it.TrangThai == "Chờ duyệt" },
                        viewModel = DonHangViewModel(),
                        viewModelUser = UserViewModel()
                    )
                    1 -> ShippingOrdersScreen(
                        searchQuery,
                        donHangList = donHangList.filter { it.TrangThai == "Đang vận chuyển" },
                        viewModel = DonHangViewModel(),
                        viewModelUser = UserViewModel()
                    )
                    2 -> DeliveredOrdersScreen(
                        searchQuery,
                        donHangList = donHangList.filter { it.TrangThai == "Thành công" },
                        onCancelOrderClick = {},
                        onReviewClick = {},
                        viewModel = DonHangViewModel(),
                        mainNavController = mainNavController,
                        viewModelUser = UserViewModel()
                    )
                    3 -> CanceledOrderScreen(
                        searchQuery,
                        donHangList = donHangList.filter { it.TrangThai == "Hủy" },
                        viewModel = DonHangViewModel(),
                        viewModelUser = UserViewModel()
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
}


@Composable
@Preview(showBackground = true)
fun OrderScreenPreview() {
    OrderScreen(
        bottomNavController = rememberNavController(),
        donHangViewModel = DonHangViewModel(),
        mainNavController = rememberNavController(),
        viewModel = UserViewModel()
    )
}