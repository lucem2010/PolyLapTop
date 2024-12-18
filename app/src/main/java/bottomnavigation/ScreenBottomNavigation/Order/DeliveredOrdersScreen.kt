package bottomnavigation.ScreenBottomNavigation.Order

import DonHang
import android.util.Log
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import model.AppConfig
import model.DonHangCT
import viewmodel.DonHangViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DeliveredOrdersScreen(
    searchQuery: String,
    donHangList: List<DonHang>, // The filtered list of delivered orders
    onReviewClick: (DonHang) -> Unit, // Callback to handle review button click
    onCancelOrderClick: (DonHang) -> Unit, // Callback to handle cancel order button click
    viewModel: DonHangViewModel = viewModel() // ViewModel instance
) {
    var showDialog by remember { mutableStateOf(false) }
    var orderDetails by remember { mutableStateOf<List<DonHangCT>>(emptyList()) }

    // Observe the LiveData for order details
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
                        // Trigger the API call to fetch order details when an order is clicked
                        viewModel.getChiTietDonHang(donHang._id)
                        showDialog = true
                    },
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Display order information
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

                    // Buttons for "Đánh giá" and "Hủy đơn hàng"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Đánh giá Button
                        Button(
                            onClick = {

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8774A)),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                text = "Đánh giá",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Hủy đơn hàng Button
                        Button(
                            onClick = {

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8774A)),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                text = "Hủy đơn hàng",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    // Show Dialog with order details when the dialog state is true
    if (showDialog) {
        // Use the observed chiTietDonHang data to display in the dialog
        ChiTietDeliveredDialog(
            orderDetails = chiTietDonHang,
            onDismiss = { showDialog = false } // Close the dialog
        )
    }
}

@Composable
fun ChiTietDeliveredDialog(
    orderDetails: List<DonHangCT>, // The order details to display
    onDismiss: () -> Unit // Callback to close the dialog
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            LazyColumn {
                items(orderDetails) { chiTiet ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 3.dp, horizontal = 3.dp)
                            .fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            // Display product details in the order
//                            Text("ID Sản phẩm: ${chiTiet.idSanPhamCT.idSanPham._id}")
//                            Text("Tên sản phẩm: ${chiTiet.idSanPhamCT.idSanPham.tenSP}")
//                            chiTiet.idSanPhamCT.Ram?.let {
//                                Text("Kích thước: $it")
//                            }
//                            chiTiet.idSanPhamCT.MauSac?.let {
//                                Text("Màu sắc: $it")
//                            }
//                            Text("Số lượng: ${chiTiet.SoLuongMua}")
//                            chiTiet.TongTien?.let {
//                                Text("Tổng tiền: ${it.toInt()} VNĐ")
//                            }
//                        }
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
fun DeliveredOrderItemPreview() {

}

