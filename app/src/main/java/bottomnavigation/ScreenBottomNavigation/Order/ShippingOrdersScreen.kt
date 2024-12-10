package bottomnavigation.ScreenBottomNavigation.Order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
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

@Composable
fun ShippingOrdersScreen(
    searchQuery: String
) {
    // Giả sử bạn có một danh sách các đơn hàng đang vận chuyển
    val orders = listOf(
        ShippingOrder(
            orderId = "OD-145656",
            shippingCompany = "Giao Hàng Nhanh",
            receiverName = "Nguyễn Văn A",
            receiverPhone = "0901234567",
            address = "123 Đường ABC, Quận 1, TP. Hồ Chí Minh",
            totalPrice = 45000000.0,
            productName = "Laptop Dell XPS 13", // Thêm tên sản phẩm
            date = "10/12/2024"
        ),
        ShippingOrder(
            orderId = "OD-214656",
            shippingCompany = "Giao Hàng Tiết Kiệm",
            receiverName = "Nguyễn Văn A",
            receiverPhone = "0901234567",
            address = "123 Đường ABC, Quận 1, TP. Hồ Chí Minh",
            totalPrice = 50000000.0,
            productName = "Laptop Dell XPS 13", // Thêm tên sản phẩm
            date = "09/12/2024"
        ),
        ShippingOrder(
            orderId = "OD-314656",
            shippingCompany = "Laptop Dell XPS 13",
            receiverName = "Nguyễn Văn A",
            receiverPhone = "0901234567",
            address = "123 Đường ABC, Quận 1, TP. Hồ Chí Minh",
            totalPrice = 15000000.0,
            productName = "Laptop Dell XPS 13", // Thêm tên sản phẩm
            date = "09/12/2024"
        ),
        ShippingOrder(
            orderId = "OD-414656",
            shippingCompany = "Giao Hàng Nhanh",
            receiverName = "Nguyễn Văn A",
            receiverPhone = "0901234567",
            address = "123 Đường ABC, Quận 1, TP. Hồ Chí Minh",
            totalPrice = 15000000.0,
            productName = "Laptop Dell XPS 13", // Thêm tên sản phẩm
            date = "09/12/2024"
        ),
        ShippingOrder(
            orderId = "OD-515656",
            shippingCompany = "Giao Hàng Nhanh",
            receiverName = "Nguyễn Văn A",
            receiverPhone = "0901234567",
            address = "123 Đường ABC, Quận 1, TP. Hồ Chí Minh",
            totalPrice = 15000000.0,
            productName = "Laptop Dell XPS 13", // Thêm tên sản phẩm
            date = "09/12/2024"
        )

    )
    val filteredOrders = orders.filter { order ->
        order.productName.contains(searchQuery, ignoreCase = true)
    }
    // Trạng thái để lưu trữ đơn hàng đã chọn
    var selectedOrder by remember { mutableStateOf<ShippingOrder?>(null) }

    // Hiển thị danh sách đơn hàng
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(filteredOrders) { order ->
            ShippingOrderItem(
                orderId = order.orderId,
                shippingCompany = order.shippingCompany,
                receiverName = order.receiverName,
                receiverPhone = order.receiverPhone,
                address = order.address,
                totalPrice = order.totalPrice,
                date = order.date,
                productName = order.productName,
                onClick = { selectedOrder = order } // Cập nhật đơn hàng đã chọn khi nhấn vào
            )
        }
    }
    // Hiển thị dialog nếu có đơn hàng được chọn
    selectedOrder?.let { order ->
        ShippingOrderDetailDialog(
            order = order,
            onDismiss = { selectedOrder = null } // Đóng dialog khi người dùng nhấn nút đóng
        )
    }
}

@Composable
fun ShippingOrderItem(
    orderId: String,
    shippingCompany: String,
    receiverName: String,
    receiverPhone: String,
    address: String,
    totalPrice: Double,
    date:String,
    productName:String,
    onClick: () -> Unit // Thêm sự kiện click
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        .clickable { onClick() }, // Nhấn vào mục để kích hoạt sự kiện
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin đơn hàng
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Mã đơn hàng
                Text(text = "${orderId}",  fontWeight = FontWeight.Bold, fontSize = 14.sp)
                // Đơn vị vận chuyển và mã theo dõi
                Text(
                    text = "Vận chuyển bởi: ${shippingCompany}",

                    fontSize = 14.sp
                )
                Row {
                    Text(text = "${receiverName} - ${receiverPhone}", fontSize = 14.sp)
                }
                Text(text = "${address}", fontSize = 14.sp)

                Spacer(modifier = Modifier.width(16.dp))

                // Tien thu ho
                Text(
                    text = "Tiền thu hộ (COD): ${totalPrice.formatCurrency()}", fontSize = 14.sp
                )
                Button(
                    onClick = { /* Handle đánh giá và nhận xét */ },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFFF8774A) // Đổi màu sang #F8774A
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Nhận hàng", color = Color.White)
                }
            }


        }
    }
}
@Composable
fun ShippingOrderDetailDialog(order: ShippingOrder, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Chi tiết đơn hàng", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Mã đơn hàng: ${order.orderId}", fontSize = 14.sp)
                Text(text = "Đơn vị vận chuyển: ${order.shippingCompany}", fontSize = 14.sp)
                Text(text = "Người nhận: ${order.receiverName}", fontSize = 14.sp)
                Text(text = "SĐT: ${order.receiverPhone}", fontSize = 14.sp)
                Text(text = "Địa chỉ: ${order.address}", fontSize = 14.sp)
                Text(text = "Tiền thu hộ (COD): ${order.totalPrice.formatCurrency()}", fontSize = 14.sp)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}
data class ShippingOrder(
    val orderId: String,
    val shippingCompany: String,
    val receiverName: String,
    val receiverPhone: String,
    val address: String,
    val totalPrice: Double,
    val date:String,
    val productName:String,
)

@Composable
@Preview(showBackground = true)
fun ShippingOrderItemPreview() {
    ShippingOrderItem(
        orderId = "OD-553444",
        shippingCompany = "Giao Hàng Nhanh",
        receiverName = "Nguyễn Văn A",
        receiverPhone = "0901234567",
        address = "123 Đường ABC, Quận 1, TP. Hồ Chí Minh",
        totalPrice = 15000000.0,
        date = "09/12/2024",
        productName = "Laptop Dell XPS 13",
        onClick = {}
    )
}






