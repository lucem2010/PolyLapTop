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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.polylaptop.R
import model.District
import model.Province
import model.SharedPrefsManager
import model.User
import model.Ward
import viewmodel.LocationViewModel
import viewmodel.UserViewModel
import java.util.Calendar


@Composable
fun ThongTinCaNhan(
    navController: NavController,
    viewModel: UserViewModel,
    viewModelLocation: LocationViewModel = viewModel()
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
                UploadAvatar(viewModel, loggedInUser, token)
                ProfileCard(
                    navController = navController,
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

///Upload Avatar
@Composable
fun UploadAvatar(viewModel: UserViewModel, loggedInUser: User, token: String) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val backgroundColor = if (isDarkTheme) Color(0x991E1E1E) else Color.White
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)

    var avatarUri = remember { mutableStateOf<Uri?>(null) } // Avatar từ thư viện
    val context = LocalContext.current
    var isImageUpdated by remember { mutableStateOf(false) } // Trạng thái khi thay đổi ảnh

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                // Kiểm tra URI
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.close()
                avatarUri.value = uri
                isImageUpdated = true

                // Cập nhật Avatar của người dùng
                val updatedUser = loggedInUser.copy(Avatar = uri.toString())
                Log.d("UpdatedUser", "Thông tin người dùng đã cập nhật: $updatedUser")
                viewModel.updateUser(token, updatedUser)
            } catch (e: Exception) {
                Log.e("ImageError", "Lỗi khi truy cập ảnh từ thư viện: ${e.message}")
                Toast.makeText(context, "Không thể mở ảnh, vui lòng thử lại.", Toast.LENGTH_SHORT)
                    .show()
            }
        } ?: run {
            Log.e("ImageError", "URI ảnh không hợp lệ")
            Toast.makeText(context, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clickable {
                    galleryLauncher.launch("image/*")
                }
        ) {
            if (isImageUpdated) {
                avatarUri.value?.let { uri ->
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
            } else if (loggedInUser.Avatar.isNullOrEmpty()) {
                // Hiển thị ảnh mặc định nếu Avatar là null hoặc rỗng
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
            } else {
                // Hiển thị ảnh từ đường dẫn trong Avatar
                Image(
                    painter = rememberAsyncImagePainter(loggedInUser.Avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

///Update Thong tin ca nhan(Ho va ten, Email, SDT, Tuoi, DiaChi, DiaChiCT)
@Composable
fun ProfileCard(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    viewModelLocation: LocationViewModel = viewModel(),
    loggedInUser: User,
    token: String
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black

    // Dữ liệu hiển thị (tên tỉnh/quận/phường đã chọn)
//    val selectedProvinceName by viewModelLocation.selectedProvinceName.collectAsState(initial = "Chưa cập nhật")
//    val selectedDistrictName by viewModelLocation.selectedDistrictName.collectAsState(initial = "Chưa cập nhật")
//    val selectedWardName by viewModelLocation.selectedWardName.collectAsState(initial = "Chưa cập nhật")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        // Text họ và tên
        Text(
            text = "Họ và tên: ${loggedInUser.HoTen ?: "Chưa cập nhật"}",
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Text email
        Text(
            text = "Email: ${loggedInUser.Email ?: "Chưa cập nhật"}",
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Text số điện thoại
        Text(
            text = "Số điện thoại: ${loggedInUser.Sdt ?: "Chưa cập nhật"}",
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Text tuổi
        Text(
            text = "Tuổi: ${loggedInUser.Tuoi ?: "Chưa cập nhật"}",
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Text địa chỉ
        Text(
            text = "Địa chỉ: ${loggedInUser.DiaChi ?: "Chưa cập nhật"} ",
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Button Cập nhật lưu thông tin
        ButtonCapNhat(navController, viewModel, loggedInUser, token)
    }
}


@Composable
fun ButtonCapNhat(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    loggedInUser: User,
    token: String,
    viewModelLocation: LocationViewModel = viewModel(),
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val textDialogColor = if (isDarkTheme) Color.Black else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)

    var showDialogCapNhatThongTin by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf(loggedInUser?.HoTen ?: "") }
    var email by remember { mutableStateOf(loggedInUser?.Email ?: "") }
    var phoneNumber by remember { mutableStateOf(loggedInUser?.Sdt ?: "") }

    var tuoi by remember { mutableStateOf(loggedInUser?.Tuoi?.toString() ?: "") }

    // Các biến lưu trữ thông tin Tỉnh, Quận, Phường
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }


    val provinces by viewModelLocation.provinces.collectAsState()
    val districts by viewModelLocation.districts.collectAsState()
    val wards by viewModelLocation.wards.collectAsState()
    val addressParts = loggedInUser.DiaChi?.split(",")?.map { it.trim() }
    var diaChiTiet by remember { mutableStateOf(addressParts?.getOrNull(0) ?: "") }
    // Ensure that provinces are fetched first
    LaunchedEffect(Unit) {
        viewModelLocation.fetchProvinces()
    }




    Row(
        modifier = Modifier.padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                showDialogCapNhatThongTin = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = borderColor
            ),
            shape = RoundedCornerShape(5.dp),
        ) {
            Text(
                text = "Cập nhật",
                color = if (isDarkTheme) Color.Black else Color.White
            )
        }
    }

    if (showDialogCapNhatThongTin) {
        AlertDialog(
            onDismissRequest = { showDialogCapNhatThongTin = false },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cập nhật thông tin",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text(text = "Họ và Tên") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text(text = "Số điện thoại") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = tuoi,
                        onValueChange = { tuoi = it },
                        label = { Text(text = "Tuổi") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        singleLine = true
                    )
                    // Dropdown cho Tỉnh/Thành phố
                    // Wait for provinces to load, then show the dropdown
                    if (provinces.isNotEmpty()) {   // Dropdown for selecting Province

                        val provinceName = addressParts?.getOrNull(3) ?: ""
                        selectedProvince = provinces.find { it.ProvinceName == provinceName }
                            ?: provinces.firstOrNull()

                        DropdownMenuWithSelection(
                            items = provinces,
                            selectedItem = selectedProvince?.ProvinceName
                                ?: "lôi lỗi", // Show empty if no selection
                            onItemSelected = { province ->
                                selectedProvince = province
                                viewModelLocation.selectProvince(province)
                            },
                            label = "Chọn Tỉnh/Thành phố",
                            itemContent = { province ->
                                Text(text = province.ProvinceName, color = Color.Black)
                            },
                            viewModel = viewModel
                        )
                    } else {
                        // Optionally show a loading indicator
                        Text(text = "Loading provinces...")
                    }

                    if (selectedProvince != null) {
                        viewModelLocation.fetchDistricts(selectedProvince!!.ProvinceID)
                        val districtName = addressParts?.getOrNull(2) ?: ""
                        selectedDistrict = districts.find { it.DistrictName == districtName }
                            ?: districts.firstOrNull()
                        // Dropdown menu cho Quận/Huyện
                        DropdownMenuWithSelection(
                            items = districts,
                            selectedItem = selectedDistrict?.DistrictName.toString() ?: "",
                            onItemSelected = { district ->
                                selectedDistrict = district
                                viewModelLocation.selectDistrict(district)
                            },
                            label = "Chọn Quận/Huyện",
                            itemContent = { district ->
                                Text(
                                    text = district.DistrictName,
                                    color = textDialogColor
                                )
                            },
                            viewModel = viewModel
                        )
                    }


                    if (selectedDistrict != null) {
                        viewModelLocation.fetchWards(selectedDistrict!!.DistrictID)
                        val WardName = addressParts?.getOrNull(1) ?: ""
                        selectedWard = wards.find { it.WardName == WardName }
                            ?: wards.firstOrNull()

                        // Dropdown menu cho Phường/Xã
                        DropdownMenuWithSelection(
                            items = wards,
                            selectedItem = selectedWard?.WardName.toString() ?: "",
                            onItemSelected = { ward ->
                                selectedWard = ward
                                viewModelLocation.selectWard(ward)
                            },
                            label = "Chọn Phường/Xã",
                            itemContent = { ward ->
                                Text(
                                    text = ward.WardName,
                                    color = textDialogColor
                                )
                            },
                            viewModel = viewModel
                        )

                    }


                    OutlinedTextField(
                        value = diaChiTiet,
                        onValueChange = { diaChiTiet = it },
                        label = { Text(text = "Địa chỉ chi tiết") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showDialogCapNhatThongTin = false
                    // Xử lý cập nhật thông tin
                }) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                Button(onClick = { showDialogCapNhatThongTin = false }) {
                    Text("Hủy")
                }
            }
        )
    }

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
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    var expanded by remember { mutableStateOf(false) }
    val textDialogColor = if (isDarkTheme) Color.Black else Color.Black
    val borderDialogColor = if (isDarkTheme) Color(0x99AcACAC) else Color.Gray
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

