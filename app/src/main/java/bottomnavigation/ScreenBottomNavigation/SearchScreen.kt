package bottomnavigation.ScreenBottomNavigation

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import model.ChiTietSanPham
import model.HangSP
import viewmodel.HangViewModel
import viewmodel.SanPhamViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SanPhamViewModel = viewModel(),
    viewModelHang: HangViewModel = viewModel(),
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var activeBrand by remember { mutableStateOf<HangSP?>(null) }
    var selectedPriceRange by remember { mutableStateOf(0..200000) }

    // Fetch dữ liệu từ ViewModel
    viewModel.fetchSanPham()
    viewModelHang.fetchHang()
    viewModel.fetchChiTietSanPham()

    // Quan sát dữ liệu từ ViewModel
    val sanPhamList by viewModel.sanPhamList.observeAsState(emptyList())
    val ChiTietsanPhamList by viewModel.chitietsanphamList.observeAsState(emptyList())
    val hangList by viewModelHang.hangList.observeAsState(emptyList())

    // Tìm giá trị maxPrice từ danh sách
    val maxPrice = ChiTietsanPhamList.maxByOrNull { it.Gia }?.Gia?.takeIf { it > 0 } ?: 200000

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            SearchBar(
                query = searchQuery,
                onQueryChanged = { searchQuery = it },
                onClearQuery = { searchQuery = TextFieldValue("") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Danh sách hãng
        BrandList(
            hangs = hangList,
            onProductClick = { selectedBrand ->
                activeBrand = if (activeBrand == selectedBrand) null else selectedBrand
            }
        )

        // Thanh trượt chọn giá
        RangeSlider(priceMax = maxPrice.toInt()) {
            selectedPriceRange = it
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lọc danh sách sản phẩm dựa trên từ khóa, hãng, và giá
        val filteredProducts = ChiTietsanPhamList.filter {
            (activeBrand == null || it.idSanPham.idHangSP == activeBrand) &&
                    it.idSanPham.tenSP.contains(searchQuery.text, ignoreCase = true) &&
                    it.Gia in selectedPriceRange.start * 1000..selectedPriceRange.endInclusive * 1000
        }

        // Hiển thị danh sách sản phẩm
        ProductList(
            products = filteredProducts,
            onProductClick = { /* Xử lý khi nhấn vào sản phẩm */ }
        )
    }
}

@Composable
fun RangeSlider(priceMax: Int, onRangeChange: (IntRange) -> Unit) {
    var sliderPosition by remember { mutableStateOf(0..priceMax) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Min: ${formatCurrency("${sliderPosition.start}000")}")
            Text(text = "Max: ${formatCurrency("${sliderPosition.endInclusive}000")}")
        }

        // Sửa lỗi giá trị không hợp lệ trong RangeSlider
        androidx.compose.material3.RangeSlider(
            value = sliderPosition.start.toFloat()..sliderPosition.endInclusive.toFloat(),
            valueRange = 0f..priceMax.toFloat().coerceAtLeast(200000f),
            onValueChange = { range ->
                sliderPosition = range.start.toInt()..range.endInclusive.toInt()
                onRangeChange(sliderPosition) // Cập nhật giá trị khi thay đổi
            },
            steps = 0,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFFFA500),
                activeTrackColor = Color(0xFFFFA500),
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        )
    }
}
@Composable
fun formatCurrency(amount: String): String {
    return try {
        val number = amount.toLong() // Chuyển đổi chuỗi sang số
        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        formatter.format(number).replace("₫", " VNĐ") // Thay ₫ thành VNĐ
    } catch (e: NumberFormatException) {
        "Không hợp lệ" // Xử lý nếu chuỗi không phải là số hợp lệ
    }
}

fun getMaxPriceProduct(products: List<ChiTietSanPham>): Int? {
    val maxPrice = products.maxOfOrNull { it.Gia } // Lấy giá lớn nhất
    Log.d("SearchScreen", "Max product price: $maxPrice")
    return maxPrice?.toInt() // Chuyển từ Long sang Int
}


@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChanged: (TextFieldValue) -> Unit,
    onClearQuery: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(50.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(100.dp))
                        .background(Color.White, shape = RoundedCornerShape(100.dp)),
                    contentAlignment = Alignment.CenterStart // Đưa nội dung và placeholder sang trái
                ) {
                    if (query.text.isEmpty()) {
                        // Thay Text bằng Box để tránh lỗi
                        Box(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            androidx.compose.foundation.text.BasicText(
                                text = "Search...",
                                style = LocalTextStyle.current.copy(
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        innerTextField() // Hiển thị nội dung văn bản người dùng nhập
                    }
                }
            }
        )
    }
}
@Composable
fun ProductList(
    products: List<ChiTietSanPham>,
    onProductClick: (ChiTietSanPham) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(products) { product ->
            ProductItem(product = product, onClick = { onProductClick(product) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun BrandList(hangs: List<HangSP>, onProductClick: (HangSP) -> Unit) {
// Biến trạng thái để lưu trữ item hiện đang active
    val (activeItem, setActiveItem) = remember { mutableStateOf<HangSP?>(null) }

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(hangs) { hang ->
            BrandItem(
                hang = hang,
                active = hang == activeItem, // Chỉ true nếu item hiện tại là active
                onClick = {
                    // Kiểm tra trạng thái hiện tại
                    if (hang == activeItem) {
                        setActiveItem(null) // Nếu item đang active, đặt lại về null
                    } else {
                        setActiveItem(hang) // Nếu không, đặt item này làm active

                    }
                    onProductClick(hang) // Gọi callback khi nhấn
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}


@Composable
fun BrandItem(hang: HangSP, active: Boolean, onClick: () -> Unit) {
//    val borderColor = if (active) Color.Green else Color.Gray
    val textColor = if (active) Color.White else Color.White
    val bgColor = if (active) Color.Green else Color.Gray

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
//            .border(1.dp, color = Color.Black, RoundedCornerShape(8.dp))
            .background(bgColor, RoundedCornerShape(8.dp))
    ) {
        Text(
            text = hang.TenHang,
            fontSize = 20.sp,
            color = textColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ProductItem(
    product: ChiTietSanPham,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .shadow(elevation = 1.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "http://192.168.100.71:5000" + product.idSanPham.anhSP?.get(0),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = product.idSanPham.tenSP, fontSize = 18.sp, color = Color.Black)
            Text(text = "Hãng: ${product.idSanPham.idHangSP.TenHang}", fontSize = 15.sp, color = Color.Gray)

            // Hiển thị chi tiết sản phẩm nếu có
            Text(text = "Giá: ${formatCurrency("${product.Gia}")}", fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController())
}