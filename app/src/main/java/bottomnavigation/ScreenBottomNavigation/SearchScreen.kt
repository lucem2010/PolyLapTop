package bottomnavigation.ScreenBottomNavigation

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.polylaptop.R
import model.HangSP
import model.SanPham
import viewmodel.HangViewModel
import viewmodel.SanPhamViewModel


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SanPhamViewModel = viewModel(),
    viewModelHang: HangViewModel = viewModel()
    ) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var activeBrand by remember { mutableStateOf<HangSP?>(null) }
//    var viewModel: SanPhamViewModel = viewModel()
    val ipAddress = "http://192.168.82.14:5000"
    viewModel.fetchSanPham()
    viewModelHang.fetchHang()
    val hangList by viewModelHang.hangList.observeAsState(emptyList())
    val sanPhamList by viewModel.sanPhamList.observeAsState(emptyList())
    val chiTietSanPhamMap by viewModel.chiTietSanPhamMap.observeAsState(emptyMap())

    val anhlist = listOf(
        "https://i.pinimg.com/564x/34/dd/68/34dd681c1fe679f5f40b46199e324b6b.jpg",
        "https://i.pinimg.com/564x/9a/13/dc/9a13dc79ca4368d6c87acb2e52cadf9d.jpg",
        "https://i.pinimg.com/736x/6a/4a/92/6a4a9250679050818495017601ae0d63.jpg"
    )

    // Dummy data
    val products = sanPhamList
    Log.d("SearchScreen", "List size: ${sanPhamList}")
//        listOf(
//        SanPham("1", HangSP("12", "mac"), "ADIDAS ULTRABOOST 21", anhlist),
//        SanPham("2", HangSP("1dc2", "dell"), "ASUS LAPTOP ROG", anhlist),
//        SanPham("3", HangSP("1xzc2", "as"), "MACBOOK PRO M1", anhlist),
//    )

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
            androidx.compose.material.Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Spacer(modifier = Modifier.width(0.dp))
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

        Spacer(modifier = Modifier.height(8.dp))

        // Lọc danh sách sản phẩm dựa trên từ khóa và hãng
        val filteredProducts = products.filter {
            (activeBrand == null || it.idHangSP == activeBrand) && // Lọc theo hãng nếu có hãng active
                    it.tenSP.contains(searchQuery.text, ignoreCase = true) // Lọc theo từ khóa
        }

        // Hiển thị danh sách sản phẩm

        ProductList(products = filteredProducts, onProductClick = { /* Xử lý khi nhấn vào sản phẩm */ })
    }
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
                        .padding( vertical = 8.dp)
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
                    ){
                        innerTextField() // Hiển thị nội dung văn bản người dùng nhập
                    }
                }
            }
        )
    }
    }

@Composable
fun ProductList(products: List<SanPham>, onProductClick: (SanPham) -> Unit) {
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
    product: SanPham, onClick: () -> Unit) {
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
            model = "http://192.168.82.14:5000" + product.anhSP?.get(0),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = product.tenSP, fontSize = 18.sp, color = Color.Black)
            Text(text ="Hang: ${product.idHangSP.TenHang}" , fontSize = 15.sp, color = Color.Gray)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController())
}
