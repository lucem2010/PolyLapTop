package bottomnavigation.ScreenBottomNavigation.Order

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun PendingOrdersScreen(searchQuery: String) {

    val pendingOrders = listOf(
        PendingOrder(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            3,
            2000000.0,
            1000.0,
            200000.0
        ),
        PendingOrder(
            "Sản phẩm 27",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            4,
            2000000.0,
            1000.0,
            200000.0
        ),
        PendingOrder(
            "Sản phẩm 25",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            2,
            2000000.0,
            1000.0,
            200000.0
        ),
        PendingOrder(
            "Sản phẩm 24",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            1,
            2000000.0,
            1000.0,
            200000.0
        ),
        PendingOrder(
            "Sản phẩm 24",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            5,
            2000000.0,
            1000.0,
            200000.0
        )
    )
    // Lọc đơn hàng dựa trên searchQuery
    val filteredOrders = pendingOrders.filter { order ->
        order.productName.contains(searchQuery, ignoreCase = true)
    }
    // Trạng thái sản phẩm được chọn
    var selectedProduct by remember { mutableStateOf<PendingOrder?>(null) }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(filteredOrders) { pendingOrder ->
            PendingOrderItem(
                productName = pendingOrder.productName,
                productImage = pendingOrder.productImage,
                quantity = pendingOrder.quantity,
                pricePerUnit = pendingOrder.pricePerUnit,
                discount = pendingOrder.discount,
                totalAmount = pendingOrder.totalAmount,
                onClick = { selectedProduct = pendingOrder } // Khi nhấn, gán sản phẩm được chọn
            )
        }
    }
    // Hiển thị Dialog khi sản phẩm được chọn
    selectedProduct?.let { pendingOrder ->
        ProductDetailDialog(pendingOrder = pendingOrder, onDismiss = { selectedProduct = null })
    }
}

@Composable
fun PendingOrderItem(
    productName: String,
    productImage: String,
    quantity: Int,
    pricePerUnit: Double,
    discount: Double,
    totalAmount: Double,
    onClick: () -> Unit // Callback khi item được nhấn
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }, // Sự kiện nhấn
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hình ảnh sản phẩm
            Image(
                painter = rememberImagePainter(productImage),
                contentDescription = "Hình ảnh sản phẩm",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Tên sản phẩm và số lượng ở hai đầu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = productName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp
                    )
                    Text(text = "x$quantity")
                }

                // Giá gốc và giá giảm
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pricePerUnit.formatCurrency(),
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = (pricePerUnit - discount).formatCurrency(), fontSize = 14.sp)
                }

                // Tổng tiền
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Tổng tiền: ${totalAmount.formatCurrency()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
@Composable
fun ProductDetailDialog(pendingOrder: PendingOrder, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
                    .verticalScroll(rememberScrollState()), // Cho phép cuộn
                verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các phần tử
            ) {
                Text(
                    text = "Chi tiết sản phẩm",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Hình ảnh sản phẩm
                Image(
                    painter = rememberImagePainter(pendingOrder.productImage),
                    contentDescription = "Hình ảnh sản phẩm",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Tên sản phẩm
                Text(
                    text = "Tên sản phẩm: ${pendingOrder.productName}",
                    fontSize = 14.sp,
                )

                // Số lượng
                Text(
                    text = "Số lượng: ${pendingOrder.quantity}",
                    fontSize = 14.sp
                )

                // Giá mỗi sản phẩm
                Text(
                    text = "Giá mỗi sản phẩm: ${pendingOrder.pricePerUnit.formatCurrency()}",
                    fontSize = 14.sp
                )

                // Giảm giá
                Text(
                    text = "Giảm giá: ${pendingOrder.discount.formatCurrency()}",
                    fontSize = 14.sp
                )

                // Tổng tiền
                Text(
                    text = "Tổng tiền: ${pendingOrder.totalAmount.formatCurrency()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Đóng", fontSize = 14.sp)
            }
        },
        modifier = Modifier.fillMaxWidth(0.9f) // Điều chỉnh độ rộng của Dialog
    )
}
// Extension function để định dạng số tiền theo định dạng tiền tệ
fun Double.formatCurrency(): String {
    return String.format("%, .0f", this)
}


data class PendingOrder(
    val productName: String,
    val productImage: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val discount: Double,
    val totalAmount: Double
)

@Composable
@Preview(showBackground = true)
fun ProductDetailDialogPreview() {
    val pricePerUnit = 150000.0
    val discount = 50000.0
    val discountedPrice = pricePerUnit - discount
    ProductDetailDialog(
        pendingOrder = PendingOrder(
           productName = "Sản phẩm 1",
           productImage = "https://laptop88.vn/media/product/pro_poster_8910.jpg", // Hình ảnh sản phẩm (tạm thời)
           quantity = (1 + 1) * 2,
           pricePerUnit = 150000.0,
           discount = 50000.0,
           totalAmount = discountedPrice * ((1 + 1) * 2),
       ),
        onDismiss = {}
    )
}
@Composable
@Preview(showBackground = true)
fun OrderPreview() {
    val pricePerUnit = 150000.0
    val discount = 50000.0
    val discountedPrice = pricePerUnit - discount
    PendingOrderItem(
        productName = "Sản phẩm 1",
        productImage = "https://laptop88.vn/media/product/pro_poster_8910.jpg", // Hình ảnh sản phẩm (tạm thời)
        quantity = (1 + 1) * 2,
        pricePerUnit = 150000.0,
        discount = 50000.0,
        totalAmount = discountedPrice * ((1 + 1) * 2),
        onClick = {}
    )
}