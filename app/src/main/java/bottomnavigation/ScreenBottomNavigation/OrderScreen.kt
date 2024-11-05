package bottomnavigation.ScreenBottomNavigation

import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.polylaptop.R
import model.OrderStatus
import model.Product
import java.util.Calendar

@Composable
fun OrderScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.left),
//                contentDescription = "Back",
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable {
//                        navController.popBackStack()
//                    }
//            )

            Text(
                text = "Thông báo",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // State for the selected date
        val date = remember { mutableStateOf("Thứ hai, 18/11/2024") }

        // Function to format day of the week
        fun getDayOfWeek(dayOfWeek: Int): String {
            return when (dayOfWeek) {
                Calendar.MONDAY -> "Thứ Hai"
                Calendar.TUESDAY -> "Thứ Ba"
                Calendar.WEDNESDAY -> "Thứ Tư"
                Calendar.THURSDAY -> "Thứ Năm"
                Calendar.FRIDAY -> "Thứ Sáu"
                Calendar.SATURDAY -> "Thứ Bảy"
                Calendar.SUNDAY -> "Chủ Nhật"
                else -> ""
            }
        }

        // DatePickerDialog
        val datePickerDialog = remember {
            android.app.DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    // Update the calendar to selected date
                    calendar.set(selectedYear, selectedMonth, selectedDay)

                    // Get the day of the week
                    val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))

                    // Format the selected date with day of the week
                    date.value = "$dayOfWeek, $selectedDay/${selectedMonth + 1}/$selectedYear"
                },
                year,
                month,
                day
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        // Row for Date Input
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Date TextField
            Row(
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextField(
                    value = date.value,
                    onValueChange = { newValue -> date.value = newValue },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                // Calendar Icon Button
                IconButton(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendar",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp),
                        tint = Color.Black
                    )

                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        var selectedProduct by remember { mutableStateOf<Product?>(null) }
        var showDialog by remember { mutableStateOf(false) }

        // Dữ liệu sản phẩm
        val products = listOf(
            Product("Laptop Dell", "Core i7", 1, R.drawable.pro1, OrderStatus.PROCESSING),
            Product("MacBook Pro", "M1 Chip", 1, R.drawable.pro2, OrderStatus.SHIPPING),
            Product("Asus Zenbook", "Core i5", 2, R.drawable.pro3, OrderStatus.COMPLETED),
            Product("HP Spectre", "Core i7", 1, R.drawable.pro4, OrderStatus.COMPLETED)
        )

        // Danh sách sản phẩm
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            items(products) { product ->
                OrderConfirmationItem(product) {
                    selectedProduct = product
                    showDialog = true
                }
                Spacer(modifier = Modifier.height(8.dp)) // Thêm khoảng cách giữa các sản phẩm
            }
        }

        // Hiển thị dialog nếu cần
        if (showDialog && selectedProduct != null) {
            ProductDetailDialog(
                product = selectedProduct!!,
                onDismiss = { showDialog = false }
            )
        }
    }

}

@Composable
fun OrderConfirmationItem(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() } // Thêm sự kiện nhấp vào
    ) {
        // Hình ảnh sản phẩm
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = "${product.name} Image",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin sản phẩm
        Column {
            // Trạng thái
            Text(
                text = when (product.status) {
                    OrderStatus.PROCESSING -> "Đang xử lý"
                    OrderStatus.SHIPPING -> "Đang giao hàng"
                    OrderStatus.COMPLETED -> "Đặt hàng thành công"
                },
                color = when (product.status) {
                    OrderStatus.PROCESSING -> Color(0xFFFFA500) // Màu cam
                    OrderStatus.SHIPPING -> Color(0xFF4682B4)  // Màu xanh dương
                    OrderStatus.COMPLETED -> Color(0xFF2A9D8F) // Màu xanh lá cây
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // Tên sản phẩm và mô tả
            Text(
                text = "${product.name} | ${product.description}",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black
            )

            // Giá sản phẩm
            Text(
                text = "${product.quantity} sản phẩm",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProductDetailDialog(product: Product, onDismiss: () -> Unit) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        text = {
//            Column(
//            ) {
//                Image(
//                    painter = painterResource(id = product.imageRes),
//                    contentDescription = null,
//                    modifier = Modifier.size(100.dp)
//                )
//                Text(text = "${product.name}")
//                Text(text = "Giá: ${product.quantity} sản phẩm")
//                Text(
//                    text = "Trạng thái: ${
//                        when (product.status) {
//                            OrderStatus.PROCESSING -> "Đang xử lý"
//                            OrderStatus.SHIPPING -> "Đang giao hàng"
//                            OrderStatus.COMPLETED -> "Đặt hàng thành công"
//                        }
//                    }"
//                )
//                Text(text = "Mô tả: ${product.description}")
//            }
//        },
//        confirmButton = {
//            Box(
//                modifier = Modifier
//                    .background(
//                        color = Color(0xFF809C7056), // Solid color
//                        shape = RoundedCornerShape(5.dp) // Rounded corners
//                    )
//                    .border(
//                        border = BorderStroke(2.dp, Color(0xFF9C7056)), // Stroke color
//                        shape = RoundedCornerShape(5.dp) // Same shape as background
//                    )
//                    .clickable(
//                        onClick = onDismiss
//                    )
//                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding inside the button
//            ) {
//                Text(
//                    text = "Đóng",
//                    color = Color.White, // Text color
//                    fontWeight = FontWeight.Bold, // Text style
//                    style = TextStyle(fontSize = 13.sp), // Text size
//                    textAlign = TextAlign.Center // Center text
//                )
//            }
//        },
//        modifier = Modifier
//            .width(300.dp)
//            .height(300.dp),
//    )
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp), // Padding to space it from screen edges
            shape = RoundedCornerShape(8.dp),
            color = Color.White, // Background color of the dialog
            elevation = 8.dp // Elevation for shadow effect
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Scrollable if content overflows
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image display with larger size
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)), // Rounded corners for the image
                        contentScale = ContentScale.Fit // Scale image to fit within bounds
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Space below the image

                // Product name
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Product quantity
                Text(text = "Giá: ${product.quantity} sản phẩm")

                // Product status
                Text(
                    text = "Trạng thái: ${
                        when (product.status) {
                            OrderStatus.PROCESSING -> "Đang xử lý"
                            OrderStatus.SHIPPING -> "Đang giao hàng"
                            OrderStatus.COMPLETED -> "Đặt hàng thành công"
                        }
                    }"
                )

                // Product description
                Text(text = "Mô tả: ${product.description}")

                Spacer(modifier = Modifier.height(16.dp)) // Space before the button

                // Close button
                Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF809C7056), // Solid color
                        shape = RoundedCornerShape(5.dp) // Rounded corners
                    )
                    .border(
                        border = BorderStroke(2.dp, Color(0xFF9C7056)), // Stroke color
                        shape = RoundedCornerShape(5.dp) // Same shape as background
                    )
                    .clickable(
                        onClick = onDismiss
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding inside the button
            ) {
                Text(
                    text = "Đóng",
                    color = Color.White, // Text color
                    fontWeight = FontWeight.Bold, // Text style
                    style = TextStyle(fontSize = 13.sp), // Text size
                    textAlign = TextAlign.Center // Center text
                )
            }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OrderScreenPreview() {
    OrderScreen(navController = rememberNavController())
}