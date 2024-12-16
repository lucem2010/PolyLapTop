package bottomnavigation.ScreenBottomNavigation.Setting

import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.lightColors
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import com.example.polylaptop.R
import model.AppConfig
import model.District
import model.Message
import model.Province
import model.SharedPrefsManager
import model.User
import model.Ward
import viewmodel.LocationViewModel
import viewmodel.UserViewModel

@Composable
fun ThongTinCaNhan(
    navController: NavController,
    viewModel: UserViewModel,
    viewModelLocation: LocationViewModel = viewModel(),
) {
    val context = LocalContext.current
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val avatarUrl by remember { mutableStateOf<String?>(loggedInUser?.Avatar) } // Trạng thái avatar URL

    MaterialTheme(
        colors = if (isDarkTheme) darkColors() else lightColors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Header(navController = navController, viewModel)
            if (loggedInUser != null && token != null) {
                ProfileCard(
                    viewModel = viewModel,
                    viewModelLocation = viewModelLocation,
                    loggedInUser, token
                )
            }

        }
    }
}

///Header
@Composable
fun Header(navController: NavController, viewModel: UserViewModel) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val iconColor = if (isDarkTheme) Color.White else Color.Black
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


@Composable
fun ProfileCard(
    viewModel: UserViewModel = viewModel(),
    viewModelLocation: LocationViewModel = viewModel(),
    loggedInUser: User,
    token: String
) {
    val context = LocalContext.current
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val textDialogColor = if (isDarkTheme) Color.Black else Color.Black

    // Đảm bảo giá trị người dùng được lấy từ SharedPreferences khi màn hình load lại
    var fullName by remember { mutableStateOf(loggedInUser.HoTen ?: "") }
    var email by remember { mutableStateOf(loggedInUser.Email ?: "") }
    var phoneNumber by remember { mutableStateOf(loggedInUser.Sdt ?: "") }
    var tuoi by remember { mutableStateOf(loggedInUser.Tuoi?.toString() ?: "") }
    var diaChiTiet by remember { mutableStateOf(loggedInUser.DiaChi ?: "") }

    var avatarUri by remember { mutableStateOf<Uri?>(null) } // Avatar từ thư viện
//    var avatarUrl by remember { mutableStateOf<String?>(loggedInUser.Avatar) }
    var avatarUrl by remember {
        mutableStateOf<String?>(AppConfig.ipAddress+loggedInUser.Avatar)
    }

    // Các biến lưu trữ thông tin Tỉnh, Quận, Phường
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }

    val selectedProvinceName by viewModelLocation.selectedProvinceName.collectAsState()
    val selectedDistrictName by viewModelLocation.selectedDistrictName.collectAsState()
    val selectedWardName by viewModelLocation.selectedWardName.collectAsState()

    val provinces by viewModelLocation.provinces.collectAsState()
    val districts by viewModelLocation.districts.collectAsState()
    val wards by viewModelLocation.wards.collectAsState()
    val avatar by viewModel.avatar // Quan sát danh sách tin nhắn

    LaunchedEffect(selectedProvince) {
        selectedDistrict = null
        selectedWard = null
        selectedProvince?.let { province ->
            viewModelLocation.fetchDistricts(province.ProvinceID)
        }
    }
    LaunchedEffect(avatar) {
        avatar?.let {
//            Log.d("IT", "ProfileCard: $it")
            avatarUrl = it
        }
    }

    LaunchedEffect(selectedDistrictName) {
        selectedDistrictName?.let { districtName ->
            val newDistrict = districts.find { it.DistrictName == districtName }
            if (newDistrict != selectedDistrict) {
                selectedDistrict = newDistrict
                viewModelLocation.fetchWards(newDistrict?.DistrictID ?: 0)
            }
        }
    }

    LaunchedEffect(viewModelLocation.wards.collectAsState().value) {
        val firstWard = viewModelLocation.wards.value.firstOrNull()
        firstWard?.let {
            selectedWard = it
            viewModelLocation.selectWard(it)
        }
    }

    LaunchedEffect(selectedWardName) {
        selectedWardName?.let {
            selectedWard = wards.find { it.WardName == selectedWardName }
        }
    }

    LaunchedEffect(Unit) {
        val addressParts = loggedInUser.DiaChi?.split(",")?.map { it.trim() }
        val detailAddress = addressParts?.getOrNull(0) ?: ""
        val wardName = addressParts?.getOrNull(1) ?: ""
        val districtName = addressParts?.getOrNull(2) ?: ""
        val provinceName = addressParts?.getOrNull(3) ?: ""
        diaChiTiet = detailAddress
        viewModelLocation.updateSelectedProvince(provinceName)
        viewModelLocation.updateSelectedDistrict(districtName)
        viewModelLocation.updateSelectedWard(wardName)
    }


    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(it)

            // Kiểm tra MIME type
            if (mimeType == null || !mimeType.startsWith("image/")) {
                Toast.makeText(context, "Vui lòng chọn file hình ảnh.", Toast.LENGTH_SHORT).show()
                return@let
            }

            // Kiểm tra nếu ảnh không thay đổi
            if (avatarUri == it) {
                Toast.makeText(context, "Ảnh này đã được chọn trước đó.", Toast.LENGTH_SHORT).show()
                return@let
            }

            avatarUri = it
            Log.d("ImageSelection", "Image selected: $it")

            // Kiểm tra token
            if (token.isBlank()) {
                Toast.makeText(context, "Token không hợp lệ. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show()
                return@let
            }

            viewModel.uploadAvatar(
                context = context,
                token = token,
                avatarUri = it,
                onSuccess = { uploadedAvatarUrl ->
//                    avatarUrl = uploadedAvatarUrl // Cập nhật lại URL avatar
//                    Log.d("AvatarUpload", "Avatar URL updated: $avatarUrl") // Log để kiểm tra giá trị mới
                    Toast.makeText(context, "Tải lên thành công: $uploadedAvatarUrl", Toast.LENGTH_SHORT).show()
                },
                onError = { error ->
                    Toast.makeText(context, "Lỗi tải lên: $error", Toast.LENGTH_SHORT).show()
                }
            )
        } ?: run {
            Toast.makeText(context, "Không có hình ảnh nào được chọn", Toast.LENGTH_SHORT).show()
        }
    }




    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        // Avatar selection
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { galleryLauncher.launch("image/*") }
        ) {

            val ipAddress = AppConfig.ipAddress // Địa chỉ IP từ AppConfig
            val defaultAvatarUrl = "$ipAddress/defaultAvatar.png" // URL của ảnh mặc định
            Log.d("avatarUrl", "ProfileCard: $avatarUrl")
// Hiển thị hình ảnh avatar từ URL nếu có, nếu không sẽ hiển thị ảnh mặc định
            AsyncImage(
                model = avatarUrl ?:defaultAvatarUrl, // Hiển thị avatar từ URL mới
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(SnackbarDefaults.backgroundColor),

            )


        }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable fields
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Họ và Tên") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
        )
        OutlinedTextField(
            value = tuoi,
            onValueChange = { tuoi = it },
            label = { Text("Tuổi") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
        )

        // Dropdown for Province
        DropdownMenuWithSelection(
            items = provinces,
            selectedItem = selectedProvinceName,
            onItemSelected = { province -> viewModelLocation.selectProvince(province) },
            itemContent = { province ->
                Text(
                    text = province.ProvinceName,
                    color = textDialogColor
                )
            },
            viewModel = viewModel,
        )

        // Dropdown for District
        DropdownMenuWithSelection(
            items = districts,
            selectedItem = selectedDistrictName,
            onItemSelected = { district -> viewModelLocation.selectDistrict(district) },
            itemContent = { district ->
                Text(
                    text = district.DistrictName,
                    color = textDialogColor
                )
            },
            viewModel = viewModel,
        )

        // Dropdown for Ward
        DropdownMenuWithSelection(
            items = wards,
            selectedItem = selectedWardName,
            onItemSelected = { ward -> viewModelLocation.selectWard(ward) },
            itemContent = { ward -> Text(text = ward.WardName, color = textDialogColor) },
            viewModel = viewModel,
        )

        OutlinedTextField(
            value = diaChiTiet,
            onValueChange = { diaChiTiet = it },
            label = { Text("Địa chỉ chi tiết") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (fullName.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty()) {
                    // Cập nhật thông tin người dùng
                    val updatedUser = loggedInUser.copy(
                        HoTen = fullName,
                        Email = email,
                        Sdt = phoneNumber,
                        Tuoi = tuoi.toIntOrNull(),
                        DiaChi = "$diaChiTiet, $selectedWardName, $selectedDistrictName, $selectedProvinceName"
                    )

                    // Chỉ cập nhật người dùng
                    viewModel.updateUser(
                        token = token,
                        user = updatedUser,
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Thông tin đã được lưu!",
                                Toast.LENGTH_SHORT
                            ).show()
                            SharedPrefsManager.saveLoginInfo(context, updatedUser, token)
                        },
                        onError = {
                            Toast.makeText(
                                context,
                                "Lỗi khi cập nhật thông tin người dùng.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        ) {
            Text("Lưu")
        }
    }
}


@Composable
fun <T> DropdownMenuWithSelection(
    items: List<T>,
    selectedItem: String?,
    onItemSelected: (T) -> Unit,
    itemContent: @Composable (T) -> Unit,
    label: String = "Chọn...",
    viewModel: UserViewModel,
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val textDialogColor = if (isDarkTheme) Color.Black else Color.Black
    val borderDialogColor = if (isDarkTheme) Color(0x99AcACAC) else Color.Gray
    var expanded by remember { mutableStateOf(false) }
    // TextField để hiển thị selected item, với biểu tượng dropdown
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = selectedItem ?: label,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .align(Alignment.CenterVertically),
            textStyle = TextStyle(fontSize = 12.sp, color = textDialogColor),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.Transparent, // Nền trong suốt
                cursorColor = borderDialogColor, // Màu con trỏ
                focusedBorderColor = borderDialogColor, // Màu đường viền khi focus
                unfocusedBorderColor = Color.Gray // Màu đường viền khi không focus
            ),
            enabled = false,  // Đảm bảo không thể chỉnh sửa trực tiếp
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
        )
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
}

@Composable
@Preview(showBackground = true)
fun ThongTinCaNhanPreview() {
    ThongTinCaNhan(navController = rememberNavController(), viewModel = UserViewModel())
}