package bottomnavigation.ScreenBottomNavigation.Order

import DonHang
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import model.AppConfig
import model.DonHangCT
import viewmodel.DonHangViewModel
import viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ShippingOrdersScreen(
    searchQuery: String,
    donHangList: List<DonHang>,
    viewModel: DonHangViewModel,
    viewModelUser: UserViewModel
) {
    // Lọc các đơn hàng có trạng thái "Đang vận chuyển"
    val chiTietDonHang by viewModel.chiTietDonHangLiveData.observeAsState(emptyList())
    val errorMessage by viewModel.errorLiveData.observeAsState("")
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardBackgroundColor = if (isDarkTheme) Color(0xFF424242) else Color.White
    var showDialog by remember { mutableStateOf(false) }
    var selectedOrderId by remember { mutableStateOf<String?>(null) }
    val filteredList = filterOrdersByDate(donHangList, searchQuery)
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(filteredList) { donHang ->
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .fillMaxWidth()
                    .clickable { // Handle the click here
                        selectedOrderId = donHang._id
                        selectedOrderId?.let { id ->
                            viewModel.getChiTietDonHang(id)
                            showDialog = true
                        }
                    },
                backgroundColor = cardBackgroundColor,
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(cardBackgroundColor)
                ) {
                    Text(
                        text = "ID Đơn hàng: ${donHang._id}",
                        style = MaterialTheme.typography.body1,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Trạng thái: ${donHang.TrangThai}",
                        color = textColor,
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Ngày đặt: ${
                            SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            ).format(donHang.NgayDatHang)
                        }",
                        color = textColor,
                        style = MaterialTheme.typography.body2
                    )
                    donHang.tongTien?.let {
                        Text(
                            text = "Tổng tiền: ${it.toInt()} VNĐ",
                            color = textColor,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }

    // Hiển thị dialog chi tiết đơn hàng
    if (showDialog) {
        ChiTietShippingDialog(
            chiTietList = chiTietDonHang,
            onDismiss = { showDialog = false },
            viewModelUser = viewModelUser
        )
    }
    // Hiển thị thông báo lỗi nếu có
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ChiTietShippingDialog(
    chiTietList: List<DonHangCT>,
    onDismiss: () -> Unit,
    viewModelUser: UserViewModel
) {
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
            ) {
                items(chiTietList) { chiTiet ->
                    Column(
                        modifier = Modifier
                            .padding(vertical = 3.dp, horizontal = 3.dp)
                            .fillMaxWidth()
                            .border(
                                BorderStroke(1.dp, Color.LightGray), // Sử dụng borderColor
                                shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                            )
                            .background(backgroundColor),
                    ) {
                        Row(modifier = Modifier.padding(3.dp)) {
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
                                    color = textColor
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${chiTiet.idSanPhamCT.idSanPham.idHangSP.TenHang}",
                                        style = MaterialTheme.typography.body2,
                                        color = textColor,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Text(
                                        text = "x${chiTiet.SoLuongMua}",
                                        style = MaterialTheme.typography.body2,
                                        color = textColor,
                                        fontWeight = FontWeight.Normal
                                    )
                                }

                            }
                        }
                        chiTiet.TongTien?.let {
                            Text(
                                text = "Tổng tiền: ${it.toInt()} VNĐ",
                                style = MaterialTheme.typography.body2,
                                color = textColor,
                                modifier = Modifier.align(Alignment.End).padding(3.dp)
                            )
                        }
                    }
                }
            }
        },
        backgroundColor = backgroundColor,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Đóng", color = textColor)
            }
        }
    )
}






