package bottomnavigation.ScreenBottomNavigation.Order

import DonHang
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import model.AppConfig
import model.DonHangCT
import viewmodel.DonHangViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CanceledOrderScreen(
    searchQuery: String,
    donHangList: List<DonHang>, // The filtered list of canceled orders
    viewModel: DonHangViewModel // Pass the ViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var orderDetails by remember { mutableStateOf<List<DonHangCT>>(emptyList()) }

    // Observe the chiTietDonHangLiveData to update the order details when available
    val chiTietDonHang by viewModel.chiTietDonHangLiveData.observeAsState(emptyList())
    val filteredList = filterOrdersByDate(donHangList, searchQuery)
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp)) {
        items(filteredList) { donHang ->
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        // Khi click vào một item, gọi API lấy chi tiết đơn hàng đã hủy
                        viewModel.getChiTietDonHang(donHang._id)
                        // Cập nhật chi tiết đơn hàng và hiển thị dialog
                        orderDetails = chiTietDonHang
                        showDialog = true
                    },
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Hiển thị thông tin đơn hàng
                    Text(
                        text = "ID Đơn hàng: ${donHang._id}",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Trạng thái: ${donHang.TrangThai}",
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Ngày đặt: ${
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(donHang.NgayDatHang)
                        }",
                        style = MaterialTheme.typography.body2
                    )
                    donHang.tongTien?.let {
                        Text(
                            text = "Tổng tiền: ${it.toInt()} VNĐ",
                            style = MaterialTheme.typography.body2
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Mua lại đơn hàng Button
                        androidx.compose.material3.Button(
                            onClick = {

                            },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF8774A)
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                text = "Mua lại",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    // Hiển thị Dialog khi click vào một đơn hàng
    if (showDialog) {
        ChiTietCancelDialog(
            orderDetails = orderDetails, // Truyền dữ liệu chi tiết đơn hàng
            onDismiss = { showDialog = false } // Đóng dialog khi người dùng nhấn "Đóng"
        )
    }
}

@Composable
fun ChiTietCancelDialog(
    orderDetails: List<DonHangCT>, // Dữ liệu chi tiết đơn hàng
    onDismiss: () -> Unit // Hàm callback để đóng dialog
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Đóng dialog khi người dùng click ngoài
        text = {
            LazyColumn {
                items(orderDetails) { chiTiet ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
//                        Column(modifier = Modifier.padding(8.dp)) {
//                            // Hiển thị chi tiết các sản phẩm trong đơn hàng
//                            orderDetails.forEach { chiTiet ->
//                                Text(text = "Sản phẩm: ${chiTiet.idSanPhamCT.idSanPham.tenSP}")
//                                Text(text = "Số lượng: ${chiTiet.idSanPhamCT.SoLuong}")
//                                Text(text = "Giá: ${chiTiet.idSanPhamCT.Gia} VNĐ")
//                                Spacer(modifier = Modifier.height(4.dp))
//                            }
//                        }
                        Column(modifier = Modifier.padding(3.dp)) {
                            Row{
                                // Hiển thị ảnh sản phẩm đầu tiên (nếu có)
                                chiTiet.idSanPhamCT.idSanPham.anhSP?.firstOrNull()
                                    ?.let { imageUrl ->
                                        val fullImageUrl = "${AppConfig.ipAddress}$imageUrl"
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
fun CanceledOrderItemPreview() {

}