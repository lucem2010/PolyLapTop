package bottomnavigation.ScreenBottomNavigation

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.polylaptop.R
import com.example.polylaptop.ui.theme.DarkBackgroundColor
import com.example.polylaptop.ui.theme.DarkBorderColor
import com.example.polylaptop.ui.theme.DarkBorderColorAvatar
import com.example.polylaptop.ui.theme.DarkButtonColor
import com.example.polylaptop.ui.theme.DarkTextColor
import com.example.polylaptop.ui.theme.LightBackgroundColor
import com.example.polylaptop.ui.theme.LightBorderColor
import com.example.polylaptop.ui.theme.LightBorderColorAvatar
import com.example.polylaptop.ui.theme.LightButtonColor
import com.example.polylaptop.ui.theme.LightTextColor
import model.Screen
import model.SharedPrefsManager
import viewmodel.UserViewModel


@Composable
fun SettingScreen(
    bottomNavController: NavController,
    mainNavController: NavController,
    viewModel: UserViewModel
) {

    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)
    Log.d("LoginInfo", "loggedInUser: $loggedInUser")
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val backgroundColorButton = if (isDarkTheme) Color.Gray else Color(0xFFF8774A)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val borderColorAvatar = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    MaterialTheme(
        colors = if (isDarkTheme) darkColors() else lightColors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp, start = 30.dp)
            ) {
                val defaultAvatar = R.drawable.img1 // Ảnh mặc định

                if (loggedInUser != null) {
                    Image(
                        painter = painterResource(
                            id = if (loggedInUser.Avatar.isNullOrBlank()) defaultAvatar else R.drawable.img1 // Thay bằng phương pháp tải ảnh nếu Avatar không rỗng
                        ),
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(70.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = if (loggedInUser.HoTen.isNullOrBlank()) "Xin chào" else loggedInUser.HoTen,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            modifier = Modifier.padding(top = 15.dp)
                        )
                        Text(
                            text = if (loggedInUser.Email.isNullOrBlank()) "Poly Laptop" else loggedInUser.Email,
                            fontSize = 15.sp,
                            color = textColor,
                            modifier = Modifier.padding(top = 10.dp, bottom = 15.dp)
                        )
                    }
                    // Hiển thị email hoặc "Poly Laptop" nếu trống

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
                            Color.LightGray
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
                        mainNavController.navigate(Screen.ThongTinCaNhan.route)
                    },
                    viewModel = viewModel,
                )

                DividerLine()

                // Layout Đổi mật khẩu
                SettingItem(
                    iconResId = R.drawable.changepassword3,
                    title = "Đổi mật khẩu",
                    onClick = {
                        mainNavController.navigate(Screen.DoiMatKhau.route)
                    },
                    viewModel = viewModel,
                )
            }
            // Cài đặt đổi giao diện
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .width(400.dp)
                    .border(
                        BorderStroke(1.dp, Color.LightGray), // Sử dụng borderColor
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
                        contentScale = ContentScale.Fit,
                        colorFilter = if (isDarkTheme) {
                            ColorFilter.tint(Color.White) // Nếu là dark mode, icon sẽ có màu trắng
                        } else {
                            ColorFilter.tint(Color.Black) // Nếu là light mode, icon sẽ có màu đen
                        }
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Đổi giao diện",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor // Thay đổi màu chữ
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme(context = context) },
                        colors = SwitchDefaults.colors(
                            uncheckedBorderColor = Color.LightGray,// Màu vien boder
                            uncheckedThumbColor = Color.LightGray, // Màu nút khi không chọn
                            uncheckedTrackColor = Color(0xFFF8774A), // Màu nền khi không chọn
                            checkedThumbColor = Color.White, // Màu nút khi chọn
                            checkedTrackColor = Color.Gray, // Màu nền khi chọn
                            checkedBorderColor = Color.LightGray
                        ),
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .width(400.dp)
                    .border(2.dp, Color.LightGray, RoundedCornerShape(5.dp)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (token != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(backgroundColorButton)
                            .padding(start = 20.dp)
                            .clickable {
                                // Hiển thị hộp thoại xác nhận đăng xuất
                                showLogoutDialog = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logout2),
                            contentDescription = "Logout Icon",
                            modifier = Modifier
                                .size(20.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = if (isDarkTheme) {
                                ColorFilter.tint(Color.White) // Nếu là dark mode, icon sẽ có màu trắng
                            } else {
                                // Nếu là light mode, icon sẽ có màu đen
                                ColorFilter.tint(Color.Black)
                            }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Đăng xuất",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }

                // Hiển thị AlertDialog khi showLogoutDialog = true
                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = { Text(text = "Xác nhận đăng xuất") },
                        text = { Text(text = "Bạn có chắc chắn muốn đăng xuất không?") },
                        confirmButton = {
                            TextButton(onClick = {
//                            // Gọi hàm logout

                                if (token != null) {
                                    viewModel.logoutUser(
                                        token = token,
                                        onSuccess = { message ->
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                                .show()
                                            // Điều hướng đến màn hình Auth
                                            SharedPrefsManager.logoutUser(context)
                                            mainNavController.navigate(Screen.Auth.route)
                                            showLogoutDialog = false
                                        },
                                        onError = { errorMessage ->
                                            // Hiển thị thông báo lỗi
                                            Toast.makeText(
                                                context,
                                                errorMessage,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Ghi log lỗi vào Logcat
                                            Log.e("LogoutError", "Lỗi khi đăng xuất: $errorMessage")
                                        }
                                    )
                                }

                            }) {
                                Text("Có", color = Color.Red)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showLogoutDialog = false
                            }) {
                                Text("Không")
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(30.dp))
        }
    }
}

@Composable
fun SettingItem(iconResId: Int, title: String, onClick: () -> Unit, viewModel: UserViewModel) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val iconColor = if (isDarkTheme) Color.White else Color.Black
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
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(iconColor) // Apply color based on theme
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(iconColor) // Apply color based on theme
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
    SettingScreen(
        mainNavController = rememberNavController(),
        bottomNavController = rememberNavController(),
        viewModel = UserViewModel()
    )
}