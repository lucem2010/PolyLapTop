package bottomnavigation.ScreenBottomNavigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Switch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.polylaptop.R

@Composable
fun SettingScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffD9D9D9)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, start = 30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img1),
                contentDescription = "Profile Image",
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "hainguyen",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 15.dp)
                )
                Text(
                    text = "hainguyen@gmail.com",
                    fontSize = 15.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 10.dp, bottom = 15.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x80AEAEAE))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, start = 30.dp, end = 30.dp)
                .clip(RoundedCornerShape(5.dp))
                .width(400.dp)
                .border(
                    BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.7f)
                    ), // Viền màu trắng với độ mờ 70%
                    shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                ),

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Layout Thông tin cá nhân
            SettingItem(
                iconResId = R.drawable.person2,
                title = "Thông tin cá nhân",
                onClick = {
                    navController.navigate("thongTinCaNhan")
                }
            )

            DividerLine()

            // Layout Đổi mật khẩu
            SettingItem(
                iconResId = R.drawable.changepassword3,
                title = "Đổi mật khẩu",
                onClick = {
                    navController.navigate("doiMatKhau")
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                .clip(RoundedCornerShape(5.dp))
                .width(400.dp)
                .border(
                    BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.7f)
                    ), // Viền màu trắng với độ mờ 70%
                    shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Layout Yêu thích
            SettingItem(
                iconResId = R.drawable.heart3,
                title = "Yêu thích",
                onClick = {
                    navController.navigate("yeuThich")
                }
            )

            DividerLine()

            // Layout Lịch sử mua hàng
            SettingItem(
                iconResId = R.drawable.shopping1,
                title = "Lịch sử mua hàng",
                onClick = {
                    navController.navigate("lichSuMuaHang")
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                .clip(RoundedCornerShape(5.dp))
                .width(400.dp)
                .border(
                    BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.7f)
                    ), // Viền màu trắng với độ mờ 70%
                    shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Layout Trợ giúp và hỗ trợ
            SettingItem(
                iconResId = R.drawable.heart3,
                title = "Trợ giúp và hỗ trợ",
                onClick = {
                    navController.navigate("troGiupVaHoTro")
                }
            )

            DividerLine()

            // Layout Bảo mật
            SettingItem(
                iconResId = R.drawable.shopping1,
                title = "Bảo mật",
                onClick = {
                    navController.navigate("baoMat")
                }
            )
        }

        val isChecked = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                .clip(RoundedCornerShape(5.dp))
                .width(400.dp)
                .border(
                    BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.7f)
                    ), // Viền màu trắng với độ mờ 70%
                    shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mode1),
                    contentDescription = "Mode Icon",
                    modifier = Modifier
                        .size(20.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = "Đổi giao diện",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                .clip(RoundedCornerShape(5.dp))
                .width(400.dp)
                .border(
                    BorderStroke(
                        2.dp,
                        Color(0xff9C7056).copy(alpha = 0.7f)
                    ), // Viền màu trắng với độ mờ 70%
                    shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xff809C7056))
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout2),
                    contentDescription = "Logout Icon",
                    modifier = Modifier
                        .size(20.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = "Đăng xuất",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(30.dp))
    }
}

@Composable
fun SettingItem(iconResId: Int, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(5.dp)) // Bo góc 5dp
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0x80AEAEAE))
    )
}

@Composable
@Preview(showBackground = true)
fun SettingScreenPreview() {
    SettingScreen(navController = rememberNavController())
}