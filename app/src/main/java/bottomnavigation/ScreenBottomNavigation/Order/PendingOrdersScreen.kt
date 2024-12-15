package bottomnavigation.ScreenBottomNavigation.Order

import DonHang
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import model.AppConfig
import model.ChiTietSanPham
import model.DonHangCT
import model.HangSP
import model.SanPham
import model.SharedPrefsManager
import viewmodel.DonHangViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PendingOrdersScreen(
    searchQuery: String,
    donHangList: List<DonHang>,
    viewModel: DonHangViewModel
) {

    val filteredList = remember(donHangList, searchQuery) {
        mutableStateOf(filterOrdersByDate(donHangList, searchQuery))
    }




    var selectedDonHang by remember { mutableStateOf<DonHang?>(null) }
    val chiTietDonHangList by viewModel.chiTietDonHangLiveData.observeAsState(emptyList())
    var showCancelDialog by remember { mutableStateOf<DonHang?>(null) }
    val context = LocalContext.current
    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.huyDonHangResult) {
        viewModel.huyDonHangResult.observe(lifecycleOwner) { result ->
            result?.onSuccess { message ->
                if (message.isNotEmpty()) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

            result?.onFailure { exception ->
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }

            viewModel.resetHuyDonHangResult()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        items(filteredList.value) { donHang ->  // Sử dụng filteredList đã được tính toán lại
            Card(
                modifier = Modifier
                    .clickable {
                        selectedDonHang = donHang
                        viewModel.getChiTietDonHang(donHang._id)
                    }
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "ID Đơn hàng: ${donHang._id}",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        Text(
                            "Trạng thái: ${donHang.TrangThai}",
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            "Ngày đặt: ${
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    donHang.NgayDatHang
                                )
                            }",
                            style = MaterialTheme.typography.body2
                        )
                        donHang.tongTien?.let {
                            Text(
                                "Tổng tiền: ${it.toInt()} VNĐ",
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                showCancelDialog = donHang
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF8774A)),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                text = "Hủy",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    // Xử lý hiển thị dialog xác nhận hủy đơn hàng
    showCancelDialog?.let { donHang ->
        AlertDialog(
            onDismissRequest = { showCancelDialog = null },
            title = {
                Text(text = "Xác nhận hủy đơn hàng")
            },
            text = {
                Text(text = "Bạn có chắc chắn muốn hủy đơn không?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (token != null) {
                            viewModel.huyDonHang(donHang._id, token)
                            // Cập nhật lại filteredList sau khi hủy đơn hàng
                            filteredList.value = filteredList.value.filter { it._id != donHang._id }
                        }
                        showCancelDialog = null
                    }
                ) {
                    Text(text = "Đồng ý", color = Color(0xFFF8774A))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelDialog = null }
                ) {
                    Text(text = "Hủy")
                }
            }
        )
    }

    // Xử lý hiển thị chi tiết đơn hàng
    selectedDonHang?.let { donHang ->
        ChiTietPendingDialog(
            chiTietList = chiTietDonHangList,
            onDismiss = { selectedDonHang = null }
        )
    }
}




fun filterOrdersByDate(orders: List<DonHang>, searchQuery: String): List<DonHang> {
    if (searchQuery.isEmpty()) return orders
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        orders.filter { donHang ->
            val orderDate = dateFormat.format(donHang.NgayDatHang)
            orderDate.contains(searchQuery, ignoreCase = true)
        }
    } catch (e: Exception) {
        emptyList()
    }
}
@Composable
fun ChiTietPendingDialog(
    chiTietList: List<DonHangCT>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            // Hiển thị danh sách chi tiết đơn hàng trong LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(chiTietList) { chiTiet ->
                    // Hiển thị thông tin của từng sản phẩm trong một Card
                    Card(
                        modifier = Modifier
                            .padding(vertical = 3.dp, horizontal = 3.dp)
                            .fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(3.dp)) {
                            Row{
                                // Hiển thị ảnh sản phẩm đầu tiên (nếu có)
                                chiTiet.idSanPhamCT.idSanPham.anhSP?.firstOrNull()
                                    ?.let { imageUrl ->
                                        val fullImageUrl = "${AppConfig.ipAddress}$imageUrl"
                                        Log.d("Image URL", "Image URL: $fullImageUrl")
                                        AsyncImage(
                                            model = fullImageUrl,
                                            contentDescription = "Ảnh sản phẩm",
                                            modifier = Modifier
                                                .width(70.dp)
                                                .height(70.dp)
                                                .border(
                                                    1.dp,
                                                    Color.LightGray,
                                                    RoundedCornerShape(5.dp)
                                                )
                                                .clip(RoundedCornerShape(5.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                Spacer(modifier = Modifier.width(5.dp))
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "${chiTiet.idSanPhamCT.idSanPham.tenSP}",
                                        style = MaterialTheme.typography.body2,
                                        color = Color.Black
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${chiTiet.idSanPhamCT.idSanPham.idHangSP.TenHang}",
                                            style = MaterialTheme.typography.body2,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "x${chiTiet.SoLuongMua}",
                                            style = MaterialTheme.typography.body2,
                                            color = Color.Gray
                                        )
                                    }

                                }
                            }
                            chiTiet.TongTien?.let {
                                Text(
                                    text = "Tổng tiền: ${it.toInt()} VNĐ",
                                    style = MaterialTheme.typography.body2,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Đóng")
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun OrderPreview() {
    val sampleChiTietList = listOf(
        DonHangCT(
            idDonHang = "DH001",
            idSanPhamCT = ChiTietSanPham(
                _id = "CTSP001",
                idSanPham = SanPham(
                    _id = "SP001",
                    idHangSP = HangSP(
                        _id = "HANG001",
                        TenHang = "Hãng A"
                    ),
                    tenSP = "Sản phẩm A",
                    anhSP = listOf("/images/sample_image.jpg")
                ),
                MauSac = "Đỏ",
                Ram = "8GB",
                SSD = "256GB",
                ManHinh = "15.6 inch",
                SoLuong = 5,
                Gia = 15000000,
                MoTa = "Mô tả sản phẩm A"
            ),
            SoLuongMua = 2,
            TongTien = 30000000.0
        ),
        DonHangCT(
            idDonHang = "DH002",
            idSanPhamCT = ChiTietSanPham(
                _id = "CTSP002",
                idSanPham = SanPham(
                    _id = "SP002",
                    idHangSP = HangSP(
                        _id = "HANG002",
                        TenHang = "Hãng B"
                    ),
                    tenSP = "Sản phẩm B",
                    anhSP = listOf("/images/sample_image2.jpg")
                ),
                MauSac = "Xanh",
                Ram = "16GB",
                SSD = "512GB",
                ManHinh = "14 inch",
                SoLuong = 3,
                Gia = 20000000,
                MoTa = "Mô tả sản phẩm B"
            ),
            SoLuongMua = 1,
            TongTien = 20000000.0
        )
    )

    ChiTietPendingDialog(
        chiTietList = sampleChiTietList,
        onDismiss = {}
    )
}