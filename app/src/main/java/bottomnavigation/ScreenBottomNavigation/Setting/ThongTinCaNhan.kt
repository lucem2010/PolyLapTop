package bottomnavigation.ScreenBottomNavigation.Setting

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.lightColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import model.EncryptedPrefsManager.getToken
import viewmodel.LocationViewModel
import viewmodel.UserViewModel
import java.util.Calendar

@Composable
fun ThongTinCaNhan(
    navController: NavController,
    viewModel: UserViewModel,
    viewModelLocation: LocationViewModel = viewModel()
) {

    val backgroundColor = if (viewModel.isDarkTheme.value) Color(0x991E1E1E) else Color.White

    MaterialTheme(
        colors = if (viewModel.isDarkTheme.value) darkColors() else lightColors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Header(navController = navController, viewModel)

            Spacer(modifier = Modifier.height(20.dp))

            ProfileCard(
                navController = navController,
                viewModel = viewModel,
                viewModelLocation = viewModelLocation
            )
        }
    }
}

@Composable
fun Header(navController: NavController, viewModel: UserViewModel) {
    val textColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    val borderColor = if (viewModel.isDarkTheme.value) Color.LightGray else Color(0xFFF8774A)
    val iconColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
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
                .background(borderColor)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = "Back",
                tint = iconColor,
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
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

private const val REQUEST_CODE_STORAGE_PERMISSION = 1001

@Composable
fun ProfileCard(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    viewModelLocation: LocationViewModel = viewModel()
) {

    val backgroundColorButton = if (viewModel.isDarkTheme.value) Color.Gray else Color(0xFFF8774A)
    val backgroundColor = if (viewModel.isDarkTheme.value) Color(0x991E1E1E) else Color.White
    val textColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    val textDialogColor = if (viewModel.isDarkTheme.value) Color.Black else Color.Black
    val borderColor = if (viewModel.isDarkTheme.value) Color.LightGray else Color(0xFFF8774A)
    val borderColorCamera = if (viewModel.isDarkTheme.value) Color(0x991E1E1E) else Color.White
    val buttonDialogColor = if (viewModel.isDarkTheme.value) Color.Gray else Color(0xFFF8774A)
    val backgroundDialogColor = if (viewModel.isDarkTheme.value) Color.White else Color.White
    val borderDialogColor = if (viewModel.isDarkTheme.value) Color(0x99AcACAC) else Color.Gray
    val iconColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black

    val context = LocalContext.current
//    val updateState by viewModel.updateState.observeAsState()
    // State cho họ và tên
    var fullName by remember { mutableStateOf("") }///HoTen
    var showDialogHoVaTen by remember { mutableStateOf(false) }
    // State cho email
    var email by remember { mutableStateOf("") }///Email
    var showDialogEmail by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    // State cho ngày sinh
    var birthDate by remember { mutableStateOf("") }///NgaySinh
    var showDialogNgaySinh by remember { mutableStateOf(false) }
    // State cho SĐT
    var phoneNumber by remember { mutableStateOf("") }///Sdt
    var showDialogSoDienThoai by remember { mutableStateOf(false) }
//    val imageUri = remember { mutableStateOf<Uri?>(null) }
    var avatarUri = remember { mutableStateOf<Uri?>(null) }//Avatar

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
    //diachi
    var showDialogDiaChi by remember { mutableStateOf(false) }
    val provinces by viewModelLocation.provinces.collectAsState()
    val districts by viewModelLocation.districts.collectAsState()
    val wards by viewModelLocation.wards.collectAsState()
    // Dữ liệu hiển thị (giả sử là tên tỉnh/quận/phường đã được chọn)
    val selectedProvinceName by viewModelLocation.selectedProvinceName.collectAsState()
    val selectedDistrictName by viewModelLocation.selectedDistrictName.collectAsState()
    val selectedWardName by viewModelLocation.selectedWardName.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Hàm kiểm tra định dạng email
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Hàm kiểm tra định dạng SDT
    fun isValidPhoneNumber(phone: String): Boolean {
        // Kiểm tra số điện thoại có đúng 10 chữ số và bắt đầu bằng số 0
        return phone.matches(Regex("^0\\d{9}$"))
    }

    // Cập nhật diaChi khi các lựa chọn thay đổi

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        val context = LocalContext.current
        val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
        var isImageUpdated by remember { mutableStateOf(false) }///// Trạng thái khi thay đổi ảnh
        // Camera launcher
        val cameraLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            bitmap?.let {
                imageBitmap.value =
                    it.asImageBitmap() // Chuyển Bitmap thành ImageBitmap và cập nhật
                avatarUri.value = null // Reset URI vì chúng ta đang sử dụng Bitmap
                isImageUpdated = true // Đánh dấu đã thay đổi ảnh
            }
        }

        // Gallery launcher
        val galleryLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                // Xử lý URI trả về từ thư viện
                try {
                    // Đảm bảo rằng URI có thể truy cập được, ví dụ sử dụng ContentResolver để kiểm tra
                    val inputStream = context.contentResolver.openInputStream(uri)
                    inputStream?.close() // Nếu không có lỗi, URI hợp lệ
                    avatarUri.value = uri // Lưu URI ảnh từ thư viện
                    imageBitmap.value = null // Reset Bitmap vì chúng ta đang sử dụng URI
                    isImageUpdated = true // Đánh dấu đã thay đổi ảnh
                } catch (e: Exception) {
                    Log.e("ImageError", "Lỗi khi truy cập ảnh từ thư viện: ${e.message}")
                    Toast.makeText(
                        context,
                        "Không thể mở ảnh, vui lòng thử lại.",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Log.e("ImageError", "Lỗi khi truy cập ảnh từ thư viện: ${e.message}")
                    Toast.makeText(
                        context,
                        "Đã xảy ra lỗi khi mở ảnh. Vui lòng thử lại.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                Log.e("ImageError", "URI ảnh không hợp lệ")
                Toast.makeText(context, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show()
            }
        }
        // Camera permission launcher
        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch() // Mở camera nếu quyền được cấp
                Toast.makeText(context, "Quyền truy cập Camera đã được cấp!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    context,
                    "Quyền truy cập Camera đã bị từ chối. Vui lòng cấp quyền trong cài đặt.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                // Chuyển đến trang cài đặt ứng dụng để cấp quyền
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
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
                                .background(backgroundColor),
                            contentScale = ContentScale.Crop
                        )
                    } ?: avatarUri.let { uri ->
                        // Hiển thị ảnh từ thư viện
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(backgroundColor),
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
                            .border(2.dp, borderColor, CircleShape)
                            .background(backgroundColor),
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
                        .background(
                            borderColorCamera,
                            CircleShape
                        )
                        .padding(8.dp)
                        .clickable { cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }, // Hành động khi click
                    tint = iconColor // Màu của icon
                )
            }
            // Button để chọn ảnh từ thư viện
            Button(
                onClick = {
                    galleryLauncher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = borderColor // Đổi màu sang #F8774A
                ),
                shape = RoundedCornerShape(5.dp) // Bo góc 10.dp
            ) {
                Text(
                    text = "Thay ảnh đại diện",
                    color = if (viewModel.isDarkTheme.value) {
                        Color.Black // Nếu là dark mode, icon sẽ có màu trắng
                    } else {
                        Color.White // Nếu là light mode, icon sẽ có màu đen
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // LayoutHoVaTen
        LayoutHoVaTen(
            fullName = fullName,
            onFullNameClick = { showDialogHoVaTen = true },// Mở dialog họ và tên,
            viewModel
        )
        //layoutEmail
        LayoutEmail(
            email = email,
            onEmailClick = { showDialogEmail = true },
            viewModel
        )
        //layoutSoDienThoai
        LayoutSoDienThoai(
            phoneNumber = phoneNumber,
            onphoneNumberClick = { showDialogSoDienThoai = true },
            viewModel
        )
        // layoutNgaySinh
        LayoutNgaySinh(
            birthDate = selectedDate.value.toString(),
            onNgaySinhClick = { showDialogNgaySinh = true }, // Mở dialog ngày sinh
            viewModel
        )
        // layoutDiaChi
        LayoutDiaChi(
            diaChi = "${selectedProvinceName}/${selectedDistrictName}/${selectedWardName}",
            onDiaChiClick = { showDialogDiaChi = true },
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {  // Gọi updateUserInfo từ viewModel để cập nhật thông tin
                // Lấy token từ SharedPreferences
                val token = getToken(context) // Hàm getToken để lấy token

                // Kiểm tra nếu không có token
                if (token.isNullOrEmpty()) {
                    errorMessage = "Token không hợp lệ hoặc hết hạn. Vui lòng đăng nhập lại."
                    isLoading = false
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    return@Button
                }
//                viewModel.updateUser(
//                    token = "Bearer $token",
//                    hoTen = fullName,
//                    ngaySinh = birthDate,
//                    email = email,
//                    sdt = phoneNumber,
//                    diaChi = "$selectedProvinceName/$selectedDistrictName/$selectedWardName",
//                    avatarUri = avatarUri.value,
//                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = borderColor // Đổi màu sang #F8774A
            ),
            shape = RoundedCornerShape(5.dp) // Bo góc 10.dp
        ) {
            Text(
                text = "Lưu thông tin", color = if (viewModel.isDarkTheme.value) {
                    Color.Black // Nếu là dark mode, icon sẽ có màu trắng
                } else {
                    Color.White // Nếu là light mode, icon sẽ có màu đen
                }
            )
        }
        // Hiển thị trạng thái cập nhật
    }
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
//    LaunchedEffect(updateState) {
//        updateState?.onSuccess {
//            val userInfo =
//                "Tên: ${fullName}, Email: ${email}, SDT: ${phoneNumber}, NgaySinh: ${birthDate}, DiaChi:  ${selectedProvinceName} / ${selectedDistrictName} / ${selectedWardName},Avatar: ${avatarUri.value}"
//            Log.d("ProfileUpdate", "Cập nhật thành công với thông tin: $userInfo")
//            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
//        }?.onFailure { exception ->
//            // Ghi lỗi vào Logcat
//            Log.e("ProfileUpdate", "Lỗi cập nhật: ${exception.message}", exception)
//            Toast.makeText(context, "Lỗi: ${exception.message}", Toast.LENGTH_SHORT).show()
//            // Cập nhật lỗi vào UI (nếu cần)
//            errorMessage = "Lỗi: ${exception.message}"
//        }
//    }
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
                        color = textDialogColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Input cho tên người dùng
                    TextField(
                        value = fullName,
                        onValueChange = { newValue ->
                            fullName = newValue
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp, borderDialogColor,
                                RoundedCornerShape(5.dp)
                            ),
                        textStyle = TextStyle(fontSize = 16.sp, color = textDialogColor),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Hiển thị số lượng ký tự
                    Text(
                        text = "${fullName.length}/50",
                        modifier = Modifier.align(Alignment.End),
                        color = if (viewModel.isDarkTheme.value) Color.Gray else Color.Gray,
                        fontSize = 12.sp
                    )
                }
            },
            backgroundColor = backgroundDialogColor,
            confirmButton = {
                // Nút Lưu
                Button(
                    onClick = {
                        showDialogHoVaTen = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        "Lưu",
                        color = if (viewModel.isDarkTheme.value) Color.White else Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                // Nút Hủy
                Button(
                    onClick = { showDialogHoVaTen = false },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        "Hủy",
                        color = if (viewModel.isDarkTheme.value) Color.White else Color.Black
                    )
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
                        color = textDialogColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = email.toString(),
                        onValueChange = { newValue ->
                            email = newValue
                            emailError = !isValidEmail(newValue) // Kiểm tra lỗi định dạng email
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, borderDialogColor, RoundedCornerShape(5.dp)),
                        textStyle = TextStyle(fontSize = 16.sp, color = textDialogColor),
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
            backgroundColor = backgroundDialogColor,
            confirmButton = {
                // Nút Lưu
                Button(
                    onClick = {
                        showDialogEmail = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                // Nút Hủy
                Button(
                    onClick = { showDialogEmail = false },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
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
                        color = textDialogColor
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
                            .border(1.dp, borderDialogColor, RoundedCornerShape(5.dp)),
                        textStyle = TextStyle(fontSize = 16.sp, color = textDialogColor),
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
            backgroundColor = backgroundDialogColor,
            confirmButton = {
                // Nút Lưu
                Button(
                    onClick = {
                        showDialogSoDienThoai = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                // Nút Hủy
                Button(
                    onClick = { showDialogSoDienThoai = false },
                    colors = ButtonDefaults.buttonColors(containerColor =buttonDialogColor),
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
                        color = textDialogColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundDialogColor, shape = RoundedCornerShape(8.dp))
                    ) {
                        TextField(
                            value = selectedDate.value,
                            onValueChange = { newValue -> selectedDate.value = newValue },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, borderDialogColor, RoundedCornerShape(5.dp)),
                            textStyle = TextStyle(fontSize = 16.sp, color = textDialogColor),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
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
            backgroundColor = backgroundDialogColor,
            confirmButton = {
                Button(
                    onClick = {
                        showDialogNgaySinh = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialogNgaySinh = false },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Hủy", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
        )
    }
    // Dialog cho địa chỉ
    LaunchedEffect(Unit) {
        viewModelLocation.fetchProvinces()
    }
    if (showDialogDiaChi) {
        AlertDialog(
            onDismissRequest = { showDialogDiaChi = false },
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
                            viewModelLocation.selectProvince(province)  // Truyền đối tượng tỉnh vào viewModel
                        },
                        itemContent = { province -> Text(text = province.ProvinceName, color = textDialogColor) }, // Hiển thị tên tỉnh
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // Drop-down menu cho Quận/Huyện
                    Text("Quận/Huyện", fontSize = 12.sp, color = Color.Black)
                    DropdownMenuWithSelection(
                        items = districts,
                        selectedItem = selectedDistrictName?.let { it },
                        onItemSelected = { district ->
                            viewModelLocation.selectDistrict(district)
                        },
                        itemContent = { district -> Text(text = district.DistrictName, color = textDialogColor) },
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // Drop-down menu cho Phường/Xã
                    Text("Phường/Xã", fontSize = 12.sp, color = Color.Black)
                    DropdownMenuWithSelection(
                        items = wards,
                        selectedItem = selectedWardName?.let { it },
                        onItemSelected = { ward ->
                            viewModelLocation.selectWard(ward)
                        },
                        itemContent = { ward -> Text(text = ward.WardName, color = textDialogColor) },
                        viewModel = viewModel
                    )
                }
            },
            backgroundColor = backgroundDialogColor,
            confirmButton = {
                Button(
                    onClick = { showDialogDiaChi = false },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialogDiaChi = false },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonDialogColor),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Hủy", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun LayoutHoVaTen(
    fullName: String,
    onFullNameClick: () -> Unit,
    viewModel: UserViewModel
) {
    val iconColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    val textColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
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
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Họ và tên", fontSize = 12.sp, color = textColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = fullName,
                fontSize = 14.sp,
                color = textColor,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onFullNameClick() },
            tint = iconColor
        )
    }
}

@Composable
fun LayoutEmail(
    email: String,
    onEmailClick: () -> Unit,
    viewModel: UserViewModel
) {
    val iconColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    val textColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
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
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Email", fontSize = 12.sp, color = textColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = email,
                fontSize = 14.sp,
                color = textColor,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onEmailClick() },
            tint = iconColor
        )
    }
}

@Composable
fun LayoutSoDienThoai(
    phoneNumber: String,
    onphoneNumberClick: () -> Unit,
    viewModel: UserViewModel
) {
    val iconColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    val textColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
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
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Số điện thoại", fontSize = 12.sp, color = textColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = phoneNumber,
                fontSize = 14.sp,
                color = textColor,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onphoneNumberClick() },
            tint = iconColor
        )
    }
}

@Composable
fun LayoutNgaySinh(
    birthDate: String,
    onNgaySinhClick: () -> Unit,
    viewModel: UserViewModel
) {
    val iconColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    val textColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
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
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ngày sinh", fontSize = 12.sp, color = textColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = birthDate,
                fontSize = 14.sp,
                color = textColor,

                )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onNgaySinhClick() },
            tint = iconColor
        )
    }
}

@Composable
fun LayoutDiaChi(
    viewModelLocation: LocationViewModel = viewModel(),
    onDiaChiClick: () -> Unit,
    viewModel: UserViewModel,
    diaChi: String
) {
    val iconColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    val textColor = if (viewModel.isDarkTheme.value) Color.White else Color.Black
    // Dữ liệu hiển thị (giả sử là tên tỉnh/quận/phường đã được chọn)
    val selectedProvinceName by viewModelLocation.selectedProvinceName.collectAsState()
    val selectedDistrictName by viewModelLocation.selectedDistrictName.collectAsState()
    val selectedWardName by viewModelLocation.selectedWardName.collectAsState()

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
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Địa chỉ", fontSize = 12.sp, color = textColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = diaChi,
                fontSize = 14.sp,
                color = textColor,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Default.Edit, // Icon từ bộ Material Icons
            contentDescription = "Right Arrow Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onDiaChiClick() },
            tint = iconColor
        )
    }
}

@Composable
fun <T> DropdownMenuWithSelectionForItemType(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemDisplay: (T) -> String,
    label: String = "Chọn...",
    viewModel: UserViewModel
) {
    val textColor = if (viewModel.isDarkTheme.value) Color.Black else Color.Black
    DropdownMenuWithSelection(
        items = items,
        selectedItem = selectedItem?.let { itemDisplay(it) },
        onItemSelected = { item -> onItemSelected(item) },
        itemContent = { item -> Text(text = itemDisplay(item), color = textColor) },
        viewModel = viewModel
    )
}

@Composable
fun <T> DropdownMenuWithSelection(
    items: List<T>,
    selectedItem: String?,
    onItemSelected: (T) -> Unit,
    itemContent: @Composable (T) -> Unit,
    label: String = "Chọn...",
    viewModel: UserViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundDialogColor = if (viewModel.isDarkTheme.value) Color(0x99AcACAC) else Color.Black
    // TextField để hiển thị selected item, với biểu tượng dropdown
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = selectedItem ?: label,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, backgroundDialogColor, RoundedCornerShape(5.dp)),
            enabled = false,  // Đảm bảo không thể chỉnh sửa trực tiếp
            trailingIcon = {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, // Đảm bảo background là trong suốt để sử dụng background của modifier
                disabledTextColor = MaterialTheme.colors.onSurface,
                focusedIndicatorColor = Color.White, // Ẩn bottom border khi TextField được chọn
                unfocusedIndicatorColor = Color.White // Ẩn bottom border khi không được chọn
            ),
        )
    }

    // DropdownMenu để hiển thị các lựa chọn
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        items.forEach { item ->
            DropdownMenuItem(onClick = {
                onItemSelected(item)
                expanded = false
            }) {
                itemContent(item)
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ThongTinCaNhanPreview() {
    ThongTinCaNhan(navController = rememberNavController(), viewModel = UserViewModel())
}