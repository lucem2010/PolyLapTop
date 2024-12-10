package bottomnavigation.ScreenBottomNavigation.Order

import DonHang
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun PendingOrdersScreen( donHangList: List<DonHang>) {

    if (donHangList.isEmpty()) {
        Log.d("PendingOrdersScreen", "DonHang List is empty")
    } else {
        Log.d("PendingOrdersScreen", "DonHang List: ${donHangList.joinToString(", ") { it.toString() }}")
    }

    val products = listOf(
        Product(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            3,
            2000000.0,
            1000.0,
            200000.0
        ),
        Product(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            4,
            2000000.0,
            1000.0,
            200000.0
        ),
        Product(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            2,
            2000000.0,
            1000.0,
            200000.0
        ),
        Product(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            1,
            2000000.0,
            1000.0,
            200000.0
        ),
        Product(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            5,
            2000000.0,
            1000.0,
            200000.0
        )
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(products) { product ->
            OrderItem(
                productName = product.productName,
                productImage = product.productImage,
                quantity = product.quantity,
                pricePerUnit = product.pricePerUnit,
                discount = product.discount,
                totalAmount = product.totalAmount
            )
        }
    }
}

@Composable
fun OrderItem(
    productName: String,
    productImage: String,
    quantity: Int,
    pricePerUnit: Double,
    discount: Double,
    totalAmount: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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

// Extension function để định dạng số tiền theo định dạng tiền tệ
fun Double.formatCurrency(): String {
    return String.format("%, .0f", this)
}


data class Product(
    val productName: String,
    val productImage: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val discount: Double,
    val totalAmount: Double
)

@Composable
@Preview(showBackground = true)
fun OrderPreview() {
    val pricePerUnit = 150000.0
    val discount = 50000.0
    val discountedPrice = pricePerUnit - discount
    OrderItem(
        productName = "Sản phẩm 1",
        productImage = "https://laptop88.vn/media/product/pro_poster_8910.jpg", // Hình ảnh sản phẩm (tạm thời)
        quantity = (1 + 1) * 2,
        pricePerUnit = 150000.0,
        discount = 50000.0,
        totalAmount = discountedPrice * ((1 + 1) * 2)
    )
}