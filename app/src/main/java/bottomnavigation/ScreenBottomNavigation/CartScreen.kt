package bottomnavigation.ScreenBottomNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.polylaptop.R
import model.Grade

@Composable
fun CartScreen(navController: NavController) {
    // Danh sách sản phẩm giả lập
    val listCart = listOf(
        Grade("1", "MacBook", "15$", "Đen", R.drawable.macbook),
        Grade("2", "MacBook Air", "30$", "Bạc", R.drawable.macbook),
        Grade("3", "MacBook Pro", "40$", "Xám", R.drawable.macbook),
        Grade("4", "MacBook", "15$", "Đen", R.drawable.macbook),
//        Grade("5", "MacBook Air", "30$", "Bạc", R.drawable.macbook),
//        Grade("6", "MacBook Pro", "40$", "Xám", R.drawable.macbook),
//        Grade("7", "MacBook", "15$", "Đen", R.drawable.macbook),
//        Grade("8", "MacBook Air", "30$", "Bạc", R.drawable.macbook),
//        Grade("9", "MacBook Pro", "40$", "Xám", R.drawable.macbook),
    )

    // Giao diện cho màn hình giỏ hàng
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp), // Chừa chỗ cho Box ở dưới
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Thanh tiêu đề
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "Quay lại",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Text(
                    text = "GIỎ HÀNG",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lưới hiển thị sản phẩm
            LazyVerticalGrid(
                columns = GridCells.Fixed(1), // Một cột
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 25.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                modifier = Modifier.fillMaxSize() // Lưới tự cuộn
            ) {
                items(listCart) { model ->
                    ItemGrid(model = model) {
                        // Hành động khi click vào sản phẩm
                    }
                }
            }
        }

        // Box cố định ở dưới cùng của màn hình
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Đặt Box ở dưới cùng của màn hình
                .background(Color(0xFFEFEFEF)) // Màu nền của Box
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng cộng: 100$",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Button(
                    onClick = { /* Thanh toán */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF8774A) // Đổi màu sang #F8774A
                    ),
                    shape = RoundedCornerShape(5.dp) // Bo góc 10.dp
                ) {
                    Text(text = "Thanh toán", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ItemGrid(model: Grade, onClick: () -> Unit = {}) {
    var soLuong by remember { mutableStateOf(1) }
    var isChecked by remember { mutableStateOf(false) } // State for the checkbox

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFEFEF) // Background color of the card
        ),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(140.dp) // Increased height to fit the checkbox
            .shadow(4.dp, RoundedCornerShape(8.dp)) // Add shadow effect
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically // Center vertically
        ) {
            Image(
                painter = painterResource(id = model.gradeimg),
                contentDescription = model.gradeName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)) // Round corners of the image
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f) // Push content to the left
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = model.gradeName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(2f)
                    )

                    // Delete icon aligned with the product name
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Xóa"
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Màu: ${model.gradecolor}", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(5.dp))

                // Row for quantity buttons and price display
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if (soLuong > 1) soLuong-- // Decrease quantity but not less than 1
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFF8774A) // Background color of the button
                        ),
                        modifier = Modifier
                            .size(25.dp) // Reduce button size
                            .clip(RoundedCornerShape(8.dp)) // Round corners of the button
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.minus), // Minus icon
                            contentDescription = "Giảm",
                            tint = Color.White // Icon color
                        )
                    }

                    Text(
                        text = soLuong.toString(),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = { soLuong++ },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFF8774A) // Background color of the button
                        ),
                        modifier = Modifier
                            .size(25.dp) // Reduce button size
                            .clip(RoundedCornerShape(8.dp)) // Round corners of the button
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus), // Plus icon
                            contentDescription = "Tăng",
                            tint = Color.White // Icon color
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Price display
                    Text(
                        text = model.gradePrice,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Checkbox below the price
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text(text = "Chọn sản phẩm", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}


// Hàm Preview cho CartScreen
@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    val navController = rememberNavController() // NavController giả lập
    CartScreen(navController = navController)
}