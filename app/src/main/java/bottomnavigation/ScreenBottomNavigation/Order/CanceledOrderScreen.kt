package bottomnavigation.ScreenBottomNavigation.Order

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
fun CanceledOrderScreen() {
    val products = listOf(
        DeliveredOrder(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            1,
            30000000.0,
            15000000.0,
            200000.0
        ),
        DeliveredOrder(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            1,
            30000000.0,
            15000000.0,
            200000.0
        ),
        DeliveredOrder(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            1,
            30000000.0,
            15000000.0,
            200000.0
        ),
        DeliveredOrder(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            1,
            30000000.0,
            15000000.0,
            200000.0
        ),
        DeliveredOrder(
            "Sản phẩm 12",
            "https://laptop88.vn/media/product/pro_poster_8910.jpg",
            1,
            30000000.0,
            15000000.0,
            200000.0
        )
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(products) { product ->
            CanceledOrderItem(
                productName = product.productName,
                productImage = product.productImage,
                quantity = product.quantity,
                originalPrice = product.originalPrice,
                discountedPrice = product.discountedPrice,
                totalPrice = product.totalPrice,
            )
        }
    }
}
@Composable
fun CanceledOrderItem(
    productImage: String,
    productName: String,
    quantity: Int,
    originalPrice: Double,
    discountedPrice: Double,
    totalPrice: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        backgroundColor = Color(0xFFF9F9F9) // Màu nền nhạt để phân biệt
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar (Hình ảnh sản phẩm)
            Image(
                painter = rememberImagePainter(productImage),
                contentDescription = "Hình ảnh sản phẩm",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin sản phẩm
            Column(
                modifier = Modifier.weight(1f)
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
                        text = "${originalPrice.formatCurrency()}",
                        style = TextStyle(
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        text = "${discountedPrice.formatCurrency()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                // Tổng tiền
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Tổng tiền: ${totalPrice.formatCurrency()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = { /* Handle đánh giá và nhận xét */ },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFFF8774A) // Đổi màu sang #F8774A
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Mua lại", color = Color.White)
                }
            }
        }
    }
}

data class CanceledOrder(
    val productName: String,
    val productImage: String,
    val quantity: Int,
    val originalPrice: Double,
    val discountedPrice: Double,
    val totalPrice: Double
)

@Composable
@Preview(showBackground = true)
fun CanceledOrderItemPreview() {
    CanceledOrderItem(
        productImage = "https://laptop88.vn/media/product/pro_poster_8910.jpg",
        productName = "Laptop Dell XPS 13",
        quantity = 1,
        originalPrice = 30000000.0,
        discountedPrice = 25000000.0,
        totalPrice = 25000000.0
    )
}