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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.polylaptop.R
import model.AppConfig
import model.District
import model.Province
import model.SharedPrefsManager
import model.SharedPrefsManager.uriToFile
import model.User
import model.Ward
import viewmodel.LocationViewModel
import viewmodel.UserViewModel
import java.io.File

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
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val textDialogColor = if (isDarkTheme) Color.Black else Color.Black
    val backgroundColor = if (isDarkTheme) Color(0x991E1E1E) else Color.White
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val updateUserStatus by viewModel.updateUserStatus.observeAsState()

    // Đảm bảo giá trị người dùng được lấy từ SharedPreferences khi màn hình load lại
    var fullName by remember { mutableStateOf(loggedInUser.HoTen ?: "") }
    var email by remember { mutableStateOf(loggedInUser.Email ?: "") }
    var phoneNumber by remember { mutableStateOf(loggedInUser.Sdt ?: "") }
    var tuoi by remember { mutableStateOf(loggedInUser.Tuoi?.toString() ?: "") }
    var diaChiTiet by remember { mutableStateOf(loggedInUser.DiaChi ?: "") }

    var avatarUri by remember { mutableStateOf<Uri?>(null) }

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


    LaunchedEffect(selectedProvince) {
        selectedDistrict = null
        selectedWard = null
        selectedProvince?.let { province ->
            viewModelLocation.fetchDistricts(province.ProvinceID)
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


    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(it)

                // Kiểm tra MIME type
                if (mimeType == null || !mimeType.startsWith("image/")) {
                    Toast.makeText(context, "Vui lòng chọn file hình ảnh.", Toast.LENGTH_SHORT)
                        .show()
                    return@let
                }

                // Kiểm tra nếu ảnh không thay đổi
                if (avatarUri == it) {
                    Toast.makeText(context, "Ảnh này đã được chọn trước đó.", Toast.LENGTH_SHORT)
                        .show()
                    return@let
                }

                avatarUri = it
                Log.d("ImageSelection", "Image selected: $it")

                // Kiểm tra token
                if (token.isBlank()) {
                    Toast.makeText(
                        context,
                        "Token không hợp lệ. Vui lòng đăng nhập lại.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@let
                }


                viewModel.uploadAvatar(
                    context = context,
                    token = token,
                    avatarUri = it,
                    onSuccess = { avatarUrl ->
                        Toast.makeText(
                            context,
                            "Tải lên thành công: $avatarUrl",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Log loggedInUser sau khi tải lên thành công
                        Log.d("LoggedInUser", "LoggedInUser: $loggedInUser")
                    },
                    onError = { error ->
                        Toast.makeText(context, "Lỗi tải lên: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            } ?: run {
                Toast.makeText(context, "Không có hình ảnh nào được chọn", Toast.LENGTH_SHORT)
                    .show()
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

            val avatarUrl = loggedInUser.Avatar // Lấy đường dẫn avatar từ đối tượng người dùng

// Lấy địa chỉ IP của máy chủ
            val ipAddress = AppConfig.ipAddress  // Địa chỉ IP từ AppConfig

// Tạo URL đầy đủ cho avatar
            val fullAvatarUrl = if (avatarUrl != null && avatarUrl.isNotEmpty()) {
                // Nếu có avatarUrl, kết hợp với địa chỉ IP hoặc URL
                "$ipAddress$avatarUrl" // Kết hợp địa chỉ IP và đường dẫn avatar
            } else {
                // Nếu không có avatarUrl, dùng ảnh mặc định
                null
            }

// Hiển thị hình ảnh avatar từ URL nếu có, nếu không sẽ hiển thị ảnh mặc định
            AsyncImage(
                model = fullAvatarUrl ?: R.drawable.img1, // Nếu fullAvatarUrl là null, sử dụng ảnh mặc định
                contentDescription = "User Avatar", // Mô tả cho hình ảnh
                contentScale = ContentScale.Crop, // Cắt ảnh cho phù hợp với vùng hiển thị
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape) // Định dạng ảnh tròn
                    .background(Color.Gray), // Màu nền khi ảnh chưa tải xong
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

//        Button(
//            onClick = {
//                if (fullName.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty()) {
//                    // Nếu có avatar mới, upload avatar trước
//                    val avatarFile = uriToFile(context, avatarUri!!)
//                    avatarUri?.let { uri ->
//                        if (avatarFile != null) {
//                            viewModel.uploadAvatar(
//                                context = context,
//                                avatarFile = avatarFile,
//                                token = token,
//                                onSuccess = { newAvatarUrl ->
//                                    // Sau khi upload avatar thành công, cập nhật thông tin người dùng
//                                    val updatedUser = loggedInUser.copy(
//                                        HoTen = fullName,
//                                        Email = email,
//                                        Sdt = phoneNumber,
//                                        Tuoi = tuoi.toIntOrNull(),
//                                        DiaChi = "$diaChiTiet, $selectedWardName, $selectedDistrictName, $selectedProvinceName",
//                                        Avatar = newAvatarUrl
//                                    )
//
//                                    // Cập nhật thông tin người dùng
//                                    viewModel.updateUser(
//                                        token = token,
//                                        user = updatedUser,
//                                        onSuccess = {
//                                            // Xử lý thành công
//                                            Toast.makeText(context, "Thông tin đã được lưu!", Toast.LENGTH_SHORT).show()
//                                            SharedPrefsManager.saveLoginInfo(context, updatedUser, token)
//                                        },
//                                        onError = {
//                                            // Xử lý lỗi
//                                            Toast.makeText(context, "Lỗi khi cập nhật thông tin người dùng.", Toast.LENGTH_SHORT).show()
//                                        }
//                                    )
//                                },
//                                onError = { errorMessage ->
//                                    Toast.makeText(context, "Tải ảnh thất bại: $errorMessage", Toast.LENGTH_SHORT).show()
//                                }
//                            )
//                        }
//                    } ?: run {
//                        // Nếu không có avatar mới, chỉ cập nhật các thông tin khác
//                        val updatedUser = loggedInUser.copy(
//                            HoTen = fullName,
//                            Email = email,
//                            Sdt = phoneNumber,
//                            Tuoi = tuoi.toIntOrNull(),
//                            DiaChi = "$diaChiTiet, $selectedWardName, $selectedDistrictName, $selectedProvinceName"
//                        )
//
//                        viewModel.updateUser(
//                            token = token,
//                            user = updatedUser,
//                            onSuccess = {
//                                Toast.makeText(context, "Thông tin đã được lưu!", Toast.LENGTH_SHORT).show()
//                                SharedPrefsManager.saveLoginInfo(context, updatedUser, token)
//                            },
//                            onError = {
//                                Toast.makeText(context, "Lỗi khi cập nhật thông tin người dùng.", Toast.LENGTH_SHORT).show()
//                            }
//                        )
//                    }
//                } else {
//                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        ) {
//            Text("Lưu")
//        }
    }
}
//////  "$diaChiTiet, $selectedWardName, $selectedDistrictName, $selectedProvinceName"
///Update Thong tin ca nhan(Ho va ten, Email, SDT, Tuoi, DiaChi, DiaChiCT)
//@Composable
//fun ProfileCard(
//    navController: NavController,
//    viewModel: UserViewModel = viewModel(),
//    viewModelLocation: LocationViewModel = viewModel(),
//    loggedInUser: User,
//    token: String
//) {
//    val context = LocalContext.current
//    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
//    val textColor = if (isDarkTheme) Color.White else Color.Black
//    val updateUserStatus by viewModel.updateUserStatus.observeAsState()
//    // Lắng nghe sự thay đổi trạng thái cập nhật thông tin người dùng
//    SharedPrefsManager.getLoginInfo(context)
//    LaunchedEffect(updateUserStatus) {
//        updateUserStatus?.let {
//            if (it.isSuccess) {
//                Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
//                // Cập nhật lại thông tin đăng nhập sau khi cập nhật thành công
//                SharedPrefsManager.saveLoginInfo(context, loggedInUser, token)
//            } else if (it.isFailure) {
//                Toast.makeText(
//                    context,
//                    "Cập nhật thất bại: ${it.exceptionOrNull()?.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//    // Lấy avatar mới từ SharedPreferences (nếu có) hoặc sử dụng avatar cũ
//    val avatarUrl = SharedPrefsManager.getAvatarUrl(context) ?: loggedInUser.Avatar
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(150.dp)
//                .clip(CircleShape)
//                .background(Color.LightGray),
//            contentAlignment = Alignment.Center
//        ) {
//            if (!avatarUrl.isNullOrEmpty()) {
//                Image(
//                    painter = rememberAsyncImagePainter(avatarUrl),
//                    contentDescription = "Avatar",
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            } else {
//                Image(
//                    painter = painterResource(id = R.drawable.img1),
//                    contentDescription = "Default Avatar",
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//        Spacer(modifier = Modifier.height(20.dp))
//        // Text họ và tên
//        Text(
//            text = "Họ và tên: ${loggedInUser.HoTen ?: "Chưa cập nhật"}",
//            fontSize = 14.sp,
//            color = textColor,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        // Text email
//        Text(
//            text = "Email: ${loggedInUser.Email ?: "Chưa cập nhật"}",
//            fontSize = 14.sp,
//            color = textColor,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        // Text số điện thoại
//        Text(
//            text = "Số điện thoại: ${loggedInUser.Sdt ?: "Chưa cập nhật"}",
//            fontSize = 14.sp,
//            color = textColor,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        // Text tuổi
//        Text(
//            text = "Tuổi: ${loggedInUser.Tuoi ?: "Chưa cập nhật"}",
//            fontSize = 14.sp,
//            color = textColor,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        // Text địa chỉ
//        Text(
//            text = "Địa chỉ: ${loggedInUser.DiaChi ?: "Chưa cập nhật"} ",
//            fontSize = 14.sp,
//            color = textColor,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        // Button Cập nhật lưu thông tin
//        ButtonCapNhat(navController, viewModel, loggedInUser, token,viewModelLocation)
//    }
//}
//
//
//@Composable
//fun ButtonCapNhat(
//    navController: NavController,
//    viewModel: UserViewModel = viewModel(),
//    loggedInUser: User,
//    token: String,
//    viewModelLocation: LocationViewModel = viewModel(),
//) {
//    val context = LocalContext.current
//    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
//    val textDialogColor = if (isDarkTheme) Color.Black else Color.Black
//    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
//    val backgroundColor = if (isDarkTheme) Color(0x991E1E1E) else Color.White
//    var showDialogCapNhatThongTin by remember { mutableStateOf(false) }
//    var fullName by remember { mutableStateOf(loggedInUser?.HoTen ?: "") }
//    var email by remember { mutableStateOf(loggedInUser?.Email ?: "") }
//    var phoneNumber by remember { mutableStateOf(loggedInUser?.Sdt ?: "") }
//    var tuoi by remember { mutableStateOf(loggedInUser?.Tuoi?.toString() ?: "") }
//    var diaChiTiet by remember { mutableStateOf(loggedInUser?.DiaChi ?: "") }
//
//    // Các biến lưu trữ thông tin Tỉnh, Quận, Phường
//    var selectedProvince by remember { mutableStateOf<Province?>(null) }
//    var selectedDistrict by remember { mutableStateOf<District?>(null) }
//    var selectedWard by remember { mutableStateOf<Ward?>(null) }
//    // Dữ liệu hiển thị (giả sử là tên tỉnh/quận/phường đã được chọn)
//    val selectedProvinceName by viewModelLocation.selectedProvinceName.collectAsState()
//    val selectedDistrictName by viewModelLocation.selectedDistrictName.collectAsState()
//    val selectedWardName by viewModelLocation.selectedWardName.collectAsState()
//
//    val provinces by viewModelLocation.provinces.collectAsState()
//    val districts by viewModelLocation.districts.collectAsState()
//    val wards by viewModelLocation.wards.collectAsState()
//
//    // Khi chọn tỉnh mới, làm mới quận/huyện và phường/xã
//    LaunchedEffect(selectedProvince) {
//        // Nếu tỉnh thay đổi, reset lại quận/huyện và phường/xã
//        selectedDistrict = null
//        selectedWard = null
//        selectedProvince?.let { province ->
//            // Fetch các quận/huyện của tỉnh mới
//            viewModelLocation.fetchDistricts(province.ProvinceID)
//        }
//    }
//    // Fetch data when a new district is selected
//    LaunchedEffect(selectedDistrictName) {
//        selectedDistrictName?.let { districtName ->
//            val newDistrict = districts.find { it.DistrictName == districtName }
//            if (newDistrict != selectedDistrict) {
//                selectedDistrict = newDistrict
//                viewModelLocation.fetchWards(newDistrict?.DistrictID ?: 0)
//            }
//        }
//    }
//    // Khi phường/xã được cập nhật, tự động chọn phường đầu tiên nếu chưa có phường nào được chọn
//    LaunchedEffect(viewModelLocation.wards.collectAsState().value) {
//        val firstWard = viewModelLocation.wards.value.firstOrNull()
//        firstWard?.let {
//            selectedWard = it
//            viewModelLocation.selectWard(it)
//        }
//    }
//    // Update ward selection when a ward name is changed
//    LaunchedEffect(selectedWardName) {
//        selectedWardName?.let {
//            selectedWard = wards.find { it.WardName == selectedWardName }
//        }
//    }
//    LaunchedEffect(Unit) {
//        // Dữ liệu cho Dropdown
//        // Cắt chuỗi địa chỉ thành các phần tử
//        val addressParts = loggedInUser.DiaChi?.split(",")?.map { it.trim() }
//        // Gán các giá trị tương ứng
//        val detailAddress = addressParts?.getOrNull(0) ?: "" // Phần đầu là địa chỉ chi tiết
//        val wardName = addressParts?.getOrNull(1) ?: ""       // Phường/Xã
//        val districtName = addressParts?.getOrNull(2) ?: ""   // Quận/Huyện
//        val provinceName = addressParts?.getOrNull(3) ?: ""   // Tỉnh/Thành phố
//        // Log các giá trị cắt được
//        Log.d("AddressParts", "Detail Address: $detailAddress")
//        Log.d("AddressParts", "Ward Name: $wardName")
//        Log.d("AddressParts", "District Name: $districtName")
//        Log.d("AddressParts", "Province Name: $provinceName")
//        // Gán giá trị địa chỉ chi tiết
//        diaChiTiet = detailAddress
//        viewModelLocation.updateSelectedProvince(provinceName)
//        viewModelLocation.updateSelectedDistrict(districtName)
//        viewModelLocation.updateSelectedWard(wardName)
//    }
//    // Hàm kiểm tra định dạng email
//    fun isValidEmail(email: String): Boolean {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    }
//    // Hàm kiểm tra định dạng SDT
//    fun isValidPhoneNumber(phone: String): Boolean {
//        return phone.matches(Regex("^0\\d{9}$"))
//    }
//    var avatarUri: Uri? by remember { mutableStateOf(null) }
//    var isUploadingAvatar by remember { mutableStateOf(false) }
//    Row(
//        modifier = Modifier.padding(20.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Button(
//            onClick = {
//                showDialogCapNhatThongTin = true
//            },
//            colors = androidx.compose.material.ButtonDefaults.buttonColors(
//                backgroundColor = borderColor
//            ),
//            shape = RoundedCornerShape(5.dp),
//        ) {
//            Text(
//                text = "Cập nhật",
//                color = if (isDarkTheme) Color.Black else Color.White
//            )
//        }
//    }
//
//    if (showDialogCapNhatThongTin) {
//        AlertDialog(
//            onDismissRequest = { showDialogCapNhatThongTin = false },
//            text = {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Cập nhật thông tin",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center
//                    )
//                    // Upload Avatar
//                    UploadAvatar(viewModel, loggedInUser, token)
//                    OutlinedTextField(
//                        value = fullName,
//                        onValueChange = { fullName = it },
//                        label = { Text(text = "Họ và Tên") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        singleLine = true
//                    )
//                    OutlinedTextField(
//                        value = email,
//                        onValueChange = { email = it },
//                        label = { Text(text = "Email") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        singleLine = true
//                    )
//                    OutlinedTextField(
//                        value = phoneNumber,
//                        onValueChange = { phoneNumber = it },
//                        label = { Text(text = "Số điện thoại") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        singleLine = true
//                    )
//                    OutlinedTextField(
//                        value = tuoi,
//                        onValueChange = { tuoi = it },
//                        label = { Text(text = "Tuổi") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        singleLine = true
//                    )
//                    // Dropdown cho Tỉnh/Thành phố
//                    DropdownMenuWithSelection(
//                        items = provinces,
//                        selectedItem = selectedProvinceName,
//                        onItemSelected = { province ->
//                            viewModelLocation.selectProvince(province)
//                        },
//                        itemContent = { province ->
//                            Text(
//                                text = province.ProvinceName,
//                                color = textDialogColor
//                            )
//                        },
//                        viewModel = viewModel,
//                    )
//                    // Dropdown menu cho Quận/Huyện
//                    DropdownMenuWithSelection(
//                        items = districts,
//                        selectedItem = selectedDistrictName,
//                        onItemSelected = { district ->
//                            viewModelLocation.selectDistrict(district)
//                        },
//                        itemContent = { district ->
//                            Text(
//                                text = district.DistrictName,
//                                color = textDialogColor
//                            )
//                        },
//                        viewModel = viewModel,
//                    )
//                    // Dropdown menu cho Phường/Xã
//                    DropdownMenuWithSelection(
//                        items = wards,
//                        selectedItem = selectedWardName,
//                        onItemSelected = { ward ->
//                            viewModelLocation.selectWard(ward)
//                        },
//                        itemContent = { ward ->
//                            Text(
//                                text = ward.WardName,
//                                color = textDialogColor
//                            )
//                        },
//                        viewModel = viewModel,
//                    )
//                    OutlinedTextField(
//                        value = diaChiTiet,
//                        onValueChange = { diaChiTiet = it },
//                        label = { Text(text = "Địa chỉ chi tiết") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        singleLine = true
//                    )
//                }
//            },
//            confirmButton = {
//                Button(onClick = {
////                    val updatedUser = loggedInUser.copy(
////                        HoTen = fullName,
////                        Email = email,
////                        Sdt = phoneNumber,
////                        Tuoi = tuoi.toIntOrNull(),
////                        DiaChi = "$diaChiTiet, $selectedWardName, $selectedDistrictName, $selectedProvinceName",
////                        Avatar = avatarUri.toString()
////                    )
////                    viewModel.updateUser(
////                        token = token,
////                        user = updatedUser,
////                        onSuccess = {
////                            SharedPrefsManager.saveLoginInfo(context, updatedUser, token)
////                            showDialogCapNhatThongTin = false
////                            // Hiển thị thông báo thành công
////                            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT)
////                                .show()
////                        },
////                        onError = { errorMessage -> Log.e("UpdateError", errorMessage) }
////                    )
//                    if (isUploadingAvatar) {
//                        // Nếu đang upload avatar thì không làm gì thêm
//                        return@Button
//                    }
//                    isUploadingAvatar = true
//
//                    // Bắt đầu upload avatar và cập nhật thông tin người dùng sau khi upload thành công
//                    viewModel.uploadAvatar(token = token, file = file, context = context, onSuccess = { uploadedUri ->
//                        // Xử lý avatarUri sau khi tải lên thành công
//                        avatarUri = uploadedUri
//                        // Tiến hành updateUser với avatar mới
//                        val updatedUser = loggedInUser.copy(
//                            HoTen = fullName,
//                            Email = email,
//                            Sdt = phoneNumber,
//                            Tuoi = tuoi.toIntOrNull(),
//                            DiaChi = "$diaChiTiet, $selectedWardName, $selectedDistrictName, $selectedProvinceName",
//                            Avatar = avatarUri.toString() // Lưu Uri dưới dạng String
//                        )
//                        viewModel.updateUser(
//                            token = token,
//                            user = updatedUser,
//                            onSuccess = {
//                                SharedPrefsManager.saveLoginInfo(context, updatedUser, token)
//                                showDialogCapNhatThongTin = false
//                                Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
//                            },
//                            onError = { errorMessage ->
//                                Log.e("UpdateError", errorMessage)
//                                Toast.makeText(context, "Cập nhật thất bại: $errorMessage", Toast.LENGTH_SHORT).show()
//                            }
//                        )
//                    }, onError = { errorMessage ->
//                        Toast.makeText(context, "Lỗi khi tải lên avatar: $errorMessage", Toast.LENGTH_SHORT).show()
//                    })
//                }) {
//                    Text("Lưu")
//                }
//            },
//            dismissButton = {
//                Button(onClick = { showDialogCapNhatThongTin = false }) {
//                    Text("Hủy")
//                }
//            }
//        )
//    }
//    LaunchedEffect(Unit) {
//        viewModelLocation.fetchProvinces()
//    }
//}


//Em de note doan nay lai
@Composable
fun UserInfoLayout(
    title: String,
    value: String,
    icon: ImageVector,
    viewModel: UserViewModel
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = textColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                color = textColor,
            )
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

