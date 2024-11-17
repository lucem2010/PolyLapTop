package bottomnavigation.ScreenBottomNavigation.Setting

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.polylaptop.R
import viewmodel.LocationViewModel
import java.util.Calendar

@Composable
fun ThongTinCaNhan(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(navController = navController)

        Spacer(modifier = Modifier.height(50.dp))

        ProfileCard(
            phoneNumber = "0123456789",
            navController = navController
        )
    }
}

@Composable
fun Header(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 30.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF8774A))
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 5.dp)
            )
        }
        Text(
            text = "Thông tin cá nhân",
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileCard(
    phoneNumber: String,
    navController: NavController
) {

    val context = LocalContext.current

    // State cho họ và tên
    var fullName by remember { mutableStateOf("Nguyễn Đức Hải") }
    var showDialogHoVaTen by remember { mutableStateOf(false) }

    // State cho ngày sinh
    var birthDate by remember { mutableStateOf("22/01/2004") }
    var showDialogNgaySinh by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // State cho ngày sinh đã chọn
    val selectedDate = remember { mutableStateOf(birthDate) }

    // Khởi tạo DatePickerDialog
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                selectedDate.value = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            },
            year,
            month,
            day
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img1),
            contentDescription = "Profile Image",
            modifier = Modifier
                .padding(top = 20.dp)
                .size(150.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFF8774A), CircleShape)
                .background(Color(0xFFF8774A)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Gọi LayoutHoVaTen
        LayoutHoVaTen(
            navController = navController,
            fullName = fullName,
            onFullNameClick = { showDialogHoVaTen = true } // Mở dialog họ và tên
        )

        // layoutSoDienThoai
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Số điện thoại", fontSize = 12.sp, color = Color.Black)
            Text(
                text = phoneNumber,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 40.dp)
            )
        }

        // layoutNgaySinh
        LayoutNgaySinh(
            navController = navController,
            birthDate = selectedDate.value,
            onNgaySinhClick = { showDialogNgaySinh = true } // Mở dialog ngày sinh
        )
        LayoutDiaChi()
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { /* Thanh toán */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF8774A) // Đổi màu sang #F8774A
            ),
            shape = RoundedCornerShape(5.dp) // Bo góc 10.dp
        ) {
            Text(text = "Lưu thông tin", color = Color.White)
        }
    }

    // Dialog cho họ và tên
    if (showDialogHoVaTen) {
        AlertDialog(
            onDismissRequest = { showDialogHoVaTen = false },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cập nhật Họ và Tên",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Input cho tên người dùng
                    UserNameInput(
                        username = fullName,
                        onUsernameChange = { fullName = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Hiển thị số lượng ký tự
                    Text(
                        text = "${fullName.length}/50",
                        modifier = Modifier.align(Alignment.End),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            },
            dismissButton = {
                // Nút Lưu
                Button(
                    onClick = {
                        showDialogHoVaTen = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8774A)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                // Nút Hủy
                Button(
                    onClick = { showDialogHoVaTen = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Hủy", color = Color.White)
                }
            },

            )
    }

    // Dialog cho ngày sinh
    if (showDialogNgaySinh) {
        AlertDialog(
            onDismissRequest = { showDialogNgaySinh = false },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cập nhật ngày sinh",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        TextField(
                            value = selectedDate.value,
                            onValueChange = { newValue -> selectedDate.value = newValue },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                            textStyle = TextStyle(fontSize = 16.sp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                        IconButton(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = "Calendar",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialogNgaySinh = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8774A)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialogNgaySinh = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Hủy", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
        )
    }
}

@Composable
fun LayoutHoVaTen(
    navController: NavController,
    fullName: String,
    onFullNameClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onFullNameClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Họ và tên", fontSize = 12.sp, color = Color.Black)
        Text(
            text = fullName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .weight(1f)
                .padding(end = 20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.right),
            contentDescription = "Right Arrow",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun LayoutNgaySinh(
    navController: NavController,
    birthDate: String,
    onNgaySinhClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onNgaySinhClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Ngày sinh", fontSize = 12.sp, color = Color.Black)
        Text(
            text = birthDate,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .weight(1f)
                .padding(end = 20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.right),
            contentDescription = "Right Arrow",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun LayoutDiaChi(viewModel: LocationViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }

    // Dữ liệu hiển thị (giả sử là tên tỉnh/quận/phường đã được chọn)
    val selectedProvinceName by viewModel.selectedProvinceName.collectAsState()
    val selectedDistrictName by viewModel.selectedDistrictName.collectAsState()
    val selectedWardName by viewModel.selectedWardName.collectAsState()

    // Khi ấn vào LayoutDiaChi sẽ hiển thị DialogDiaChi
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Địa chỉ", fontSize = 12.sp, color = Color.Black)
        Text(
            text = "$selectedWardName/$selectedDistrictName/$selectedProvinceName ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .weight(1f)
                .padding(end = 20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.right),
            contentDescription = "Right Arrow",
            modifier = Modifier.size(24.dp)
        )
    }

    // Hiển thị DialogDiaChi khi showDialog là true
    if (showDialog) {
        DialogDiaChi(viewModel = viewModel, onDismiss = { showDialog = false })
    }
}

@Composable
fun DialogDiaChi(viewModel: LocationViewModel, onDismiss: () -> Unit) {
    val provinces by viewModel.provinces.collectAsState()
    val districts by viewModel.districts.collectAsState()
    val wards by viewModel.wards.collectAsState()

    val selectedProvinceName by viewModel.selectedProvinceName.collectAsState()
    val selectedDistrictName by viewModel.selectedDistrictName.collectAsState()
    val selectedWardName by viewModel.selectedWardName.collectAsState()

    // Fetch provinces on dialog load
    LaunchedEffect(Unit) {
        viewModel.fetchProvinces()
    }

    // Hiển thị Dialog
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column {
                Text(
                    text = "Cập nhật địa chỉ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Dropdown cho Tỉnh/Thành phố
                Text("Tỉnh/Thành Phố", fontSize = 12.sp, color = Color.Black)
                DropdownMenuWithSelection(
                    items = provinces,
                    selectedItem = selectedProvinceName?.let { it }, // Truyền đối tượng tỉnh vào đây
                    onItemSelected = { province ->
                        viewModel.selectProvince(province)  // Truyền đối tượng tỉnh vào viewModel
                    },
                    itemContent = { province -> Text(text = province.ProvinceName) } // Hiển thị tên tỉnh
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Drop-down menu cho Quận/Huyện
                Text("Quận/Huyện", fontSize = 12.sp, color = Color.Black)
                DropdownMenuWithSelection(
                    items = districts,
                    selectedItem = selectedDistrictName?.let { it },
                    onItemSelected = { district ->
                        viewModel.selectDistrict(district)
                    },
                    itemContent = { district -> Text(text = district.DistrictName) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Drop-down menu cho Phường/Xã
                Text("Phường/Xã", fontSize = 12.sp, color = Color.Black)
                DropdownMenuWithSelection(
                    items = wards,
                    selectedItem = selectedWardName?.let { it },
                    onItemSelected = { ward ->
                        viewModel.selectWard(ward)
                    },
                    itemContent = { ward -> Text(text = ward.WardName) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8774A)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("Hủy", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun UserNameInput(
    username: String,
    onUsernameChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        TextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp, Color.Black,
                    RoundedCornerShape(5.dp)
                ),
            textStyle = TextStyle(fontSize = 16.sp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ThongTinCaNhanPreview() {
    ThongTinCaNhan(navController = rememberNavController())
}