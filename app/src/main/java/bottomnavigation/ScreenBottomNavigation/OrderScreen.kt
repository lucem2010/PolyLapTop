package bottomnavigation.ScreenBottomNavigation


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import viewmodel.UserViewModel

@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: UserViewModel,
) {
    val tabs = listOf("Chờ xác nhận", "Đang vận chuyển", "Đã giao", "Đã hủy")
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchType by remember { mutableStateOf("Tìm kiếm theo ID Đơn hàng") }
    val searchOptions =
        listOf("Tìm kiếm theo ID Đơn hàng", "Tìm kiếm theo Tên sản phẩm", "Tìm kiếm theo Ngày")

    // Lọc danh sách đơn hàng
//    val filteredOrders = viewModel.orderList.filter { order ->
//        val matchesQuery = when (searchType) {
//            "Tìm kiếm theo ID Đơn hàng" -> order.id.contains(searchQuery, ignoreCase = true)
//            "Tìm kiếm theo Tên sản phẩm" -> order.productName.contains(
//                searchQuery,
//                ignoreCase = true
//            )
//
//            "Tìm kiếm theo Ngày" -> order.orderDate.contains(searchQuery, ignoreCase = true)
//            else -> false
//        }
//        matchesQuery && order.status == tabs[selectedTab]
//    }

    Column {
        // Ô tìm kiếm
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            //
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                label = { Text(searchType) },

                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))
            //Loc
            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ){
                IconButton(onClick = { expanded = true }, modifier = Modifier.size(30.dp).padding(top = 10.dp) ) {
                    Icon(Icons.Default.FilterAlt, contentDescription = "FilterAlt Options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    searchOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                searchType = option
                                expanded = false
                            }
                        ) {
                            Text(option)
                        }
                    }
                }
            }

        }

        // Thanh cuộn ngang cho danh mục
        Row(
            modifier = Modifier
                .height(50.dp)
                .padding(start = 10.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, item ->
                TextButton(
                    onClick = { selectedTab = index }
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
            0 -> PendingOrdersScreen(searchQuery)
            1 -> ShippingOrdersScreen(searchQuery)
            2 -> DeliveredOrdersScreen(searchQuery)
            3 ->  CanceledOrderScreen(searchQuery)
        }
    }
}

//@Composable
//fun OrderItem(order: Order) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = 4.dp
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "Mã đơn hàng: ${order.id}", fontWeight = FontWeight.Bold)
//            Text(text = "Tên sản phẩm: ${order.productName}")
//            Text(text = "Ngày đặt: ${order.orderDate}")
//            Text(text = "Trạng thái: ${order.status}")
//        }
//    }
//}


@Composable
@Preview(showBackground = true)
fun OrderScreenPreview() {
    OrderScreen(
        navController = rememberNavController(),
        viewModel = UserViewModel(),
    )
}