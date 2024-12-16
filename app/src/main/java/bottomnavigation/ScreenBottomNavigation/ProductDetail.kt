package bottomnavigation.ScreenBottomNavigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.polylaptop.R
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import model.AppConfig
import model.ChiTietSanPham
import model.DanhGia
import model.HangSP
import model.SanPham
import model.Screen
import model.SharedPrefsManager
import view.alert_dialog.CartIconWithLoginCheck
import viewmodel.CartViewModel
import viewmodel.DanhGiaViewModel
import viewmodel.SanPhamViewModel
import viewmodel.UserViewModel
import java.net.URLDecoder


@Composable
fun ProductDetail(
    navController: NavController,
    chiTietSanPhamJson: String?,
    cartViewModel: CartViewModel = viewModel(),
    sanPhamViewModel: SanPhamViewModel = viewModel(),
    danhGiaViewModel: DanhGiaViewModel = viewModel(),
    viewModel: UserViewModel
) {
    val showDialogState = remember { mutableStateOf(false) }
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val backgroundColorButton = if (isDarkTheme) Color.Gray else Color(0xFFF8774A)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val borderColorAvatar = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    // Giải mã và chuyển đổi chuỗi JSON thành đối tượng `ChiTietSanPham`
    val chiTietSanPham = chiTietSanPhamJson?.let {
        val decodedJson = URLDecoder.decode(it, "UTF-8")
        Log.d(
            "ProductDetail",
            "Decoded chiTietSanPhamJson: $decodedJson"
        ) // Log JSON sau khi decode
        try {
            val chiTiet = Json.decodeFromString<ChiTietSanPham>(decodedJson)
            Log.d(
                "ProductDetail",
                "ChiTietSanPham decoded successfully: $chiTiet"
            ) // Log đối tượng ChiTietSanPham
            chiTiet
        } catch (e: Exception) {
            Log.e("ProductDetail", "Error decoding ChiTietSanPham: ${e.message}", e)
            null
        }
    }
    val chiTietSanPhamList by sanPhamViewModel.chiTietSanPhamList.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        if (chiTietSanPham != null) {
            sanPhamViewModel.fetchChiTietSanPhamOfid(chiTietSanPham.idSanPham._id)
        }
    }
    val sanPham = chiTietSanPham?.idSanPham?.let {
        SanPham(
            _id = it._id,
            idHangSP = HangSP(
                _id = it.idHangSP._id,
                TenHang = it.idHangSP.TenHang
            ),
            tenSP = it.tenSP,
            anhSP = it.anhSP
        )
    }
    // Sử dụng ảnh đầu tiên làm ảnh chính, nếu có
    val ipAddress = AppConfig.ipAddress
    // Sử dụng ảnh đầu tiên làm ảnh chính, nếu có
    val selectedImage = remember {
        mutableStateOf(sanPham?.anhSP?.firstOrNull()?.let { "$ipAddress$it" } ?: "")
    }
    // Log giá trị
    val imageScrollState = rememberScrollState()
    chiTietSanPhamList?.let { list ->
        println("Chi tiết sản phẩm: ${list.joinToString { it.MoTa }}")
    }
    var selectedRam by remember { mutableStateOf(chiTietSanPham?.Ram) }
    var selectedSSD by remember { mutableStateOf(chiTietSanPham?.SSD) }
    var selectedManHinh by remember { mutableStateOf(chiTietSanPham?.ManHinh) }
    var selectedMauSac by remember { mutableStateOf(chiTietSanPham?.MauSac) }
    // Lọc các lựa chọn từ danh sách chi tiết sản phẩm
    val ramOptions = chiTietSanPhamList?.map { it.Ram }?.distinct() ?: emptyList()
    val ssdOptions = chiTietSanPhamList?.map { it.SSD }?.distinct() ?: emptyList()
    val manHinhOptions = chiTietSanPhamList?.map { it.ManHinh }?.distinct() ?: emptyList()
    val mauSacOptions = chiTietSanPhamList?.map { it.MauSac }?.distinct() ?: emptyList()
    var chiTietSanPhamMap by remember {
        mutableStateOf<Map<String, Pair<Double, ChiTietSanPham>>>(
            emptyMap()
        )
    }
    var showOrderDetails by remember { mutableStateOf(false) }
    // Hàm để lọc chi tiết sản phẩm
    val filteredProducts = chiTietSanPhamList?.filter {
        (selectedRam == null || it.Ram == selectedRam) &&
                (selectedSSD == null || it.SSD == selectedSSD) &&
                (selectedManHinh == null || it.ManHinh == selectedManHinh) &&
                (selectedMauSac == null || it.MauSac == selectedMauSac)
    }
    // Kiểm tra nếu có sản phẩm phù hợp
    var selectedProduct by remember { mutableStateOf<ChiTietSanPham?>(null) }
    val imgLogo = "https://vuainnhanh.com/wp-content/uploads/2023/02/logo-FPT-Polytechnic-.png"
    // Cập nhật selectedProduct
    LaunchedEffect(filteredProducts, chiTietSanPham) {
        if (filteredProducts != null) {
            selectedProduct = filteredProducts.firstOrNull { it._id == chiTietSanPham?._id }
        }
    }
    // Gọi fetchDanhGias khi selectedProduct thay đổi
    LaunchedEffect(selectedProduct) {
        selectedProduct?.let { danhGiaViewModel.fetchDanhGias(it._id) }
    }
    val danhGiaList by danhGiaViewModel.danhGias.observeAsState(emptyList())
    val context = LocalContext.current
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {

        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 30.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(borderColor)
                        .size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.left),
                        contentDescription = "Back",
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Chi tiết sản phẩm",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                AsyncImage(
                    model = imgLogo,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(50.dp), // Set size of the image
                    contentScale = ContentScale.Fit // Scale the image to fit within the size
                )

            }
        }

        // Product Image
        item {
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
            ) {
                // Hiển thị ảnh chính
                // Lấy URL của ảnh từ `selectedImage.value`, nếu có
                val imageUrl = selectedImage.value.takeIf { it.isNotEmpty() }

                if (imageUrl != null) {
                    // Hiển thị hình ảnh từ URL
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                BorderStroke(2.dp, Color.LightGray),
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }


                Spacer(modifier = Modifier.height(10.dp))

                // Hiển thị ảnh bổ sung
                sanPham?.anhSP?.let { imagesList ->
                    Row(
                        modifier = Modifier
                            .horizontalScroll(imageScrollState)
                    ) {
                        imagesList.forEach { imagePath ->
                            // Thêm ipAddress vào mỗi URL hình ảnh
                            val imageUrl = "$ipAddress$imagePath"

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                                    .clickable {
                                        // Thay đổi ảnh chính khi nhấn vào ảnh phụ
                                        selectedImage.value = imageUrl
                                    }
                                    .clip(RoundedCornerShape(5.dp))
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .size(70.dp)
                                        .border(
                                            BorderStroke(
                                                2.dp,
                                                if (selectedImage.value == imageUrl) Color(
                                                    0xFFF8774A
                                                ) else Color(0xB3FFFFFF)
                                            ),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
        // Product Information
        item {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
            ) {
                Text(
                    text = "${chiTietSanPham?.idSanPham?.tenSP}",
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = if (selectedProduct != null) {
                        "${selectedProduct!!.Gia} VND"
                    } else {
                        "Hết hàng"
                    },
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Right
                )
            }
        }
        // Dropdown cho thông số sản phẩm
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Dropdown cho RAM nếu có dữ liệu
                if (ramOptions.isNotEmpty()) {
                    DropdownOption(
                        "RAM", ramOptions, onItemSelected = { selectedRam = it },
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                // Dropdown cho Màn hình nếu có dữ liệu
                if (manHinhOptions.isNotEmpty()) {
                    DropdownOption(
                        "Màn hình",
                        manHinhOptions,
                        onItemSelected = { selectedManHinh = it },
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                // Dropdown cho SSD nếu có dữ liệu
                if (ssdOptions.isNotEmpty()) {
                    DropdownOption(
                        "Ổ cứng", ssdOptions, onItemSelected = { selectedSSD = it },
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                // Dropdown cho Màu sắc nếu có dữ liệu
                if (mauSacOptions.isNotEmpty()) {
                    DropdownOption(
                        "Màu sắc",
                        mauSacOptions,
                        onItemSelected = { selectedMauSac = it },
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }

        // Mô tả sản phẩm
        item {
            Text(
                text = "${chiTietSanPham?.MoTa}",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Justify,
                color = textColor,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxHeight() // Áp dụng fillMaxHeight cho phần mô tả để không bị thiếu chữ
            )
        }
        // Nút thêm vào giỏ hàng
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {
                // Nút "Mua ngay"
                Button(
                    onClick = {
                        // Log trước khi thực hiện các bước
                        Log.d("ButtonClick", "Button clicked. Preparing data.")
                        if (token == null) {
                            showDialogState.value = true  // Hiển thị dialog
                        } else {
                            val chiTietSanPhamMap = selectedProduct?.let {
                                mapOf(
                                    it._id to Pair(1.0, it)
                                )
                            } ?: emptyMap()

                            val chiTietSanPhamJson = Uri.encode(Gson().toJson(chiTietSanPhamMap))
                            try {
                                navController.navigate(Screen.OrderDetailsScreen.route + "/$chiTietSanPhamJson")
                            } catch (e: Exception) {
                                Log.e("ButtonClick", "Navigation error: ${e.message}")
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor // Màu cam nếu có sản phẩm, LightGray nếu không
                    ),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(
                        "Mua ngay",
                        color = textColor, // Màu chữ trắng nếu có sản phẩm, xám nếu không
                        fontSize = 18.sp,
                    )
                }
                CheckLoginDialog(token, navController, showDialogState, viewModel)
                CartIconWithLoginCheck(
                    navController = navController,
                    selectedProduct = selectedProduct,
                    cartViewModel,
                    viewModel,
                )
            }
        }
        // Reviews Section
        item {
            var showReviews by remember { mutableStateOf(false) } // State để kiểm soát hiển thị đánh giá
            Column(
                modifier = Modifier
                    .padding(top = 10.dp, end = 20.dp, start = 20.dp, bottom = 30.dp)
                    .fillMaxWidth()
            ) {
                // Nút "Xem đánh giá"
                Button(
                    onClick = { showReviews = !showReviews },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(if (showReviews) "Ẩn đánh giá" else "Xem đánh giá", color = textColor, fontSize = 18.sp)
                }
                // Hiển thị danh sách đánh giá nếu showReviews = true
                if (showReviews) {
                    // Sử dụng LazyColumn để hiển thị danh sách đánh giá
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                            .heightIn(max = 500.dp) // Hạn chế chiều cao tối đa nếu cần thiết
                    ) {
                        items(danhGiaList) { danhGia ->
                            ReviewItem(danhGia = danhGia, ipAddress)
                        }
                    }
                }
            }
        }


    }
}

// Hàm để hiển thị một đối tượng đánh giá
@Composable
fun ReviewItem(danhGia: DanhGia, ipAddress: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar người dùng (bo tròn)
            val avatarUrl =
                danhGia.idUser.Avatar?.let { "$ipAddress$it" } // Kết hợp IP với đường dẫn ảnh

// Nếu avatarUrl là null, hiển thị hình ảnh mặc định 'heart1' từ drawable
            val imagePainter = rememberImagePainter(
                data = avatarUrl
                    ?: R.drawable.img1 // Thay thế bằng hình ảnh từ drawable nếu avatarUrl là null
            )
            Image(
                painter = imagePainter,
                contentDescription = "Avatar người dùng",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape) // Bo tròn hình ảnh
                    .border(2.dp, Color.Gray, CircleShape) // Viền xung quanh avatar
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Thông tin đánh giá
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Người dùng: ${danhGia.idUser.HoTen}",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Điểm: ${danhGia.Diem}",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "Nội dung: ${danhGia.NoiDung}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
        // Hiển thị hình ảnh (nếu có)
        if (danhGia.HinhAnh.isNotEmpty()) {
            LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                items(danhGia.HinhAnh) { imageUrl ->
                    // Kết hợp IP với URL hình ảnh cho mỗi ảnh trong danh sách HinhAnh
                    val fullImageUrl = "$ipAddress$imageUrl"

                    Image(
                        painter = rememberImagePainter(data = fullImageUrl), // Sử dụng URL đầy đủ cho mỗi ảnh
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 8.dp)
                    )
                }
            }
        }
        // Đường kẻ ngang màu cam
        Divider(
            color = Color(0xFFFF5722), // Màu cam
            thickness = 1.dp, // Độ dày của đường kẻ
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownOption(
    label: String,
    options: List<String>,
    onItemSelected: (String) -> Unit,
    viewModel: UserViewModel
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    var expanded by remember { mutableStateOf(false) }
    val textDialogColor = if (isDarkTheme) Color.White else Color.Black
    val borderDialogColor = if (isDarkTheme) Color(0x99AcACAC) else Color.Gray
    var selectedOption by remember { mutableStateOf(options[0]) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Label
            Text(
                text = "$label: ",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp),
                color = textDialogColor,
            )
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth() // Đảm bảo TextField chiếm toàn bộ chiều rộng
                    .height(55.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, // Đảm bảo background là trong suốt để sử dụng background của modifier
                    disabledTextColor = borderDialogColor,
                    focusedIndicatorColor = borderDialogColor, // Ẩn bottom border khi TextField được chọn
                    unfocusedIndicatorColor = Color.Gray // Ẩn bottom border khi không được chọn
                ),
                shape = RoundedCornerShape(12.dp), // Thêm Border Radius cho TextField
                enabled = false, // Giữ trạng thái chỉ đọc
                textStyle = MaterialTheme.typography.body1.copy(
                    textAlign = TextAlign.End,
                    color = textDialogColor// Căn chỉnh văn bản sang bên phải
                ),
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(borderDialogColor)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onItemSelected(option) // Gọi callback khi người dùng chọn một giá trị
                    },
                ) {
                    Text(text = option, textAlign = TextAlign.End, color = textDialogColor)
                }
            }
        }
    }
}


@Composable
fun CheckLoginDialog(
    token: String?,
    navController: NavController,
    showDialogState: MutableState<Boolean>,
    viewModel: UserViewModel
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val backgroundColorButton = if (isDarkTheme) Color.Gray else Color(0xFFF8774A)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val borderColorAvatar = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    if (showDialogState.value) {
        AlertDialog(
            onDismissRequest = {
                showDialogState.value = false // Đóng dialog khi người dùng nhấn ngoài
            },
            title = { Text(text = "Bạn chưa đăng nhập", color = textColor) },
            text = { Text(text = "Bạn cần đăng nhập để tiếp tục.", color = textColor) },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(Screen.Auth.route)
                        showDialogState.value =
                            false // Đóng dialog sau khi chuyển đến màn hình đăng nhập
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor
                    ),
                ) {
                    Text("Đăng nhập", color = textColor)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialogState.value = false // Đóng dialog khi người dùng nhấn "Đóng"
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = borderColor
                    ),
                ) {
                    Text("Đóng", color = textColor)
                }
            },
            containerColor = backgroundColor

        )
    }
}

// Hàm mở rộng để định dạng số thập phân
fun Float.format(digits: Int) = "%.${digits}f".format(this)

@Composable
@Preview(showBackground = true)
private fun ProductDetailPreview() {
    ProductDetail(
        viewModel = UserViewModel(),
        navController = rememberNavController(),
        chiTietSanPhamJson = null,
        cartViewModel = CartViewModel(),
        sanPhamViewModel = SanPhamViewModel(),
        danhGiaViewModel = DanhGiaViewModel()
    )
}