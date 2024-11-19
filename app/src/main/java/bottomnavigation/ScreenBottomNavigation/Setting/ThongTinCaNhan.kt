package bottomnavigation.ScreenBottomNavigation.Setting

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
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

        Spacer(modifier = Modifier.height(20.dp))

        ProfileCard(
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
    navController: NavController
) {
    val context = LocalContext.current
    // State cho họ và tên
    var fullName by remember { mutableStateOf("Nguyễn Đức Hải") }
    var showDialogHoVaTen by remember { mutableStateOf(false) }
    // State cho email
    var email by remember { mutableStateOf("haindph39815@fpt.edu.vn") }
    var showDialogEmail by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    // State cho ngày sinh
    var birthDate by remember { mutableStateOf("22/01/2004") }
    var showDialogNgaySinh by remember { mutableStateOf(false) }
    // State cho SĐT
    var phoneNumber by remember { mutableStateOf("0123456789") }
    var showDialogSoDienThoai by remember { mutableStateOf(false) }

    var phoneNumberError by remember { mutableStateOf(false) }
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

    // Hàm kiểm tra định dạng email
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Hàm kiểm tra định dạng SDT
    fun isValidPhoneNumber(phone: String): Boolean {
        // Kiểm tra số điện thoại có đúng 10 chữ số và bắt đầu bằng số 0
        return phone.matches(Regex("^0\\d{9}$"))
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        ProfilePicture()
        Spacer(modifier = Modifier.height(20.dp))

        // Gọi LayoutHoVaTen
        LayoutHoVaTen(
            fullName = fullName,
            onFullNameClick = { showDialogHoVaTen = true } // Mở dialog họ và tên
        )
        //layoutEmail
        LayoutEmail(
            email = email,
            onEmailClick = { showDialogEmail = true }

        )
        //layoutSoDienThoai
        LayoutSoDienThoai(
            phoneNumber = phoneNumber,
            onphoneNumberClick = { showDialogSoDienThoai = true }

        )
        // layoutNgaySinh
        LayoutNgaySinh(
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

                ) {
                    Text(
                        text = "Cập nhật Họ và Tên",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
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
                }
            },
            confirmButton = {
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
            dismissButton = {
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
    //Dialog cho email
    if (showDialogEmail) {
        AlertDialog(
            onDismissRequest = { showDialogEmail = false },
            text = {
                Column(

                ) {
                    Text(
                        text = "Cập nhật Email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = email,
                        onValueChange = { newValue ->
                            email = newValue
                            emailError = !isValidEmail(newValue) // Kiểm tra lỗi định dạng email
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                        textStyle = TextStyle(fontSize = 16.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        isError = emailError, // Hiển thị lỗi nếu sai định dạng
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email // Bàn phím nhập email
                        ),
                    )
                    // Hiển thị lỗi nếu email sai định dạng
                    if (emailError) {
                        Text(
                            text = "Email không hợp lệ!",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
            },
            confirmButton = {
                // Nút Lưu
                Button(
                    onClick = {
                        showDialogEmail = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8774A)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                // Nút Hủy
                Button(
                    onClick = { showDialogEmail = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Hủy", color = Color.White)
                }
            },

            )
    }
    //Dialog cho SĐT
    if (showDialogSoDienThoai) {
        AlertDialog(
            onDismissRequest = { showDialogSoDienThoai = false },
            text = {
                Column(

                ) {
                    Text(
                        text = "Cập nhật số điện thoại",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = phoneNumber,
                        onValueChange = { newValue ->
                            phoneNumber = newValue
                            phoneNumberError =
                                !isValidPhoneNumber(newValue) // Kiểm tra số điện thoại hợp lệ
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                        textStyle = TextStyle(fontSize = 16.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        isError = phoneNumberError, // Hiển thị lỗi nếu không hợp lệ
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone // Chọn bàn phím số điện thoại
                        ),
                    )
                    // Hiển thị lỗi nếu số điện thoại không hợp lệ
                    if (phoneNumberError) {
                        Text(
                            text = "Số điện thoại không hợp lệ!",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
            },
            confirmButton = {
                // Nút Lưu
                Button(
                    onClick = {
                        showDialogSoDienThoai = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8774A)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                // Nút Hủy
                Button(
                    onClick = { showDialogSoDienThoai = false },
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
                ) {
                    Text(
                        text = "Cập nhật ngày sinh",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
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
            confirmButton = {
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
            dismissButton = {
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
fun ProfilePicture() {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    ///// Trạng thái khi thay đổi ảnh
    var isImageUpdated by remember { mutableStateOf(false) }
    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            imageBitmap.value = it.asImageBitmap() // Chuyển Bitmap thành ImageBitmap và cập nhật
            imageUri.value = null // Reset URI vì chúng ta đang sử dụng Bitmap
            isImageUpdated = true // Đánh dấu đã thay đổi ảnh
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri.value = uri // Lưu URI ảnh từ thư viện
            imageBitmap.value = null // Reset Bitmap vì chúng ta đang sử dụng URI
            isImageUpdated = true // Đánh dấu đã thay đổi ảnh
        }
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch() // Mở camera nếu quyền được cấp
        } else {
            Toast.makeText(context, "Quyền truy cập Camera đã bị từ chối.", Toast.LENGTH_SHORT)
                .show()
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
        ) {
            // Hiển thị ảnh dựa trên trạng thái
            if (isImageUpdated) {
                imageBitmap.value?.let { bitmap ->
                    // Hiển thị ảnh chụp từ camera
                    Image(
                        bitmap = bitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                } ?: imageUri.value?.let { uri ->
                    // Hiển thị ảnh từ thư viện
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                // Hiển thị ảnh mặc định nếu chưa thay đổi
                Image(
                    painter = painterResource(id = R.drawable.img1),
                    contentDescription = "Default Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(1.dp, Color(0x99F8774A), CircleShape)
                        .background(Color(0x99F8774A)),
                    contentScale = ContentScale.Crop
                )
            }
            // Icon camera
            Icon(
                imageVector = Icons.Default.CameraAlt, // Icon từ bộ Material Icons
                contentDescription = "Camera Icon",
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Đặt ở góc dưới bên phải
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .padding(8.dp)
                    .clickable { cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }, // Hành động khi click
                tint = Color(0xFF000000) // Màu của icon
            )
        }
        // Button để chọn ảnh từ thư viện
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x99F8774A) // Đổi màu sang #F8774A
            ),
            shape = RoundedCornerShape(5.dp) // Bo góc 10.dp
        ) {
            Text(text = "Thay ảnh đại diện", color = Color.White)
        }

    }
}

@Composable
fun LayoutHoVaTen(
    fullName: String,
    onFullNameClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Person, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp),
            tint = Color(0xFF000000) // Màu của icon
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Họ và tên", fontSize = 12.sp, color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = fullName,
                fontSize = 14.sp,
                color = Color.Black,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onFullNameClick() },
            tint = Color(0xFF000000) // Màu của icon
        )
    }
}

@Composable
fun LayoutEmail(
    email: String,
    onEmailClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Email, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp),
            tint = Color(0xFF000000) // Màu của icon
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Email", fontSize = 12.sp, color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = email,
                fontSize = 14.sp,
                color = Color.Black,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onEmailClick() },
            tint = Color(0xFF000000) // Màu của icon
        )
    }
}

@Composable
fun LayoutSoDienThoai(
    phoneNumber: String,
    onphoneNumberClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Phone, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp),
            tint = Color(0xFF000000) // Màu của icon
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Số điện thoại", fontSize = 12.sp, color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = phoneNumber,
                fontSize = 14.sp,
                color = Color.Black,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onphoneNumberClick() },
            tint = Color(0xFF000000) // Màu của icon
        )
    }
}

@Composable
fun LayoutNgaySinh(
    birthDate: String,
    onNgaySinhClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
//            .clickable { onNgaySinhClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.DateRange, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp),
            tint = Color(0xFF000000) // Màu của icon
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ngày sinh", fontSize = 12.sp, color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = birthDate,
                fontSize = 14.sp,
                color = Color.Black,

                )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onNgaySinhClick() },
            tint = Color(0xFF000000) // Màu của icon
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
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp),
            tint = Color(0xFF000000) // Màu của icon
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Địa chỉ", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "$selectedWardName/$selectedDistrictName/$selectedProvinceName ",
                fontSize = 14.sp,
                color = Color.Black,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { showDialog = true },
            tint = Color(0xFF000000) // Màu của icon
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
                    itemContent = { province -> Text(text = province.ProvinceName) }, // Hiển thị tên tỉnh
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
                    itemContent = { district -> Text(text = district.DistrictName) },
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
                    itemContent = { ward -> Text(text = ward.WardName) },
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