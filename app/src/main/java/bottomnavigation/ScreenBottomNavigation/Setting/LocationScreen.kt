package bottomnavigation.ScreenBottomNavigation.Setting

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodel.LocationViewModel

@Composable
fun LocationScreen(viewModel: LocationViewModel = viewModel()) {
    val provinces by viewModel.provinces.collectAsState()
    val districts by viewModel.districts.collectAsState()
    val wards by viewModel.wards.collectAsState()

    val selectedProvinceName by viewModel.selectedProvinceName.collectAsState()
    val selectedDistrictName by viewModel.selectedDistrictName.collectAsState()
    val selectedWardName by viewModel.selectedWardName.collectAsState()

    // Fetch provinces on screen load
    LaunchedEffect(Unit) {
        viewModel.fetchProvinces()
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Drop-down menu cho Tỉnh/Thành phố
        Text("Tỉnh/Thành Phố", fontSize = 12.sp, color = Color.Black)
        DropdownMenuWithSelection(
            items = provinces,
            selectedItem = selectedProvinceName?.let { it }, // Truyền đối tượng tỉnh vào đây
            onItemSelected = { province ->
                viewModel.selectProvince(province)  // Truyền đối tượng tỉnh vào viewModel
            },
            itemContent = { province -> Text(text = province.ProvinceName) } // Hiển thị tên tỉnh
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Drop-down menu cho Quận/Huyện
        Text("Quận/Huyện", fontSize = 12.sp, color = Color.Black)
        DropdownMenuWithSelection(
            items = districts,
            selectedItem = selectedDistrictName?.let { it },
            onItemSelected = { district ->
                viewModel.selectDistrict(district)
            },
            itemContent = { district -> Text(text = district.DistrictName) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Drop-down menu cho Phường/Xã
        Text("Phường/Xã", fontSize = 12.sp, color = Color.Black)
        DropdownMenuWithSelection(
            items = wards,
            selectedItem = selectedWardName?.let { it },
            onItemSelected = { ward ->
                viewModel.selectWard(ward)
            },
            itemContent = { ward -> Text(text = ward.WardName) }
        )
    }
}

@Composable
fun <T> DropdownMenuWithSelectionForItemType(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemDisplay: (T) -> String,
    label: String = "Chọn..."
) {
    DropdownMenuWithSelection(
        items = items,
        selectedItem = selectedItem?.let { itemDisplay(it) },
        onItemSelected = { item -> onItemSelected(item) },
        itemContent = { item -> Text(text = itemDisplay(item)) }
    )
}

@Composable
fun <T> DropdownMenuWithSelection(
    items: List<T>,
    selectedItem: String?,
    onItemSelected: (T) -> Unit,
    itemContent: @Composable (T) -> Unit,
    label: String = "Chọn..."
) {
    var expanded by remember { mutableStateOf(false) }

    // TextField để hiển thị selected item, với biểu tượng dropdown
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = selectedItem ?: label,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
            enabled = false,  // Đảm bảo không thể chỉnh sửa trực tiếp
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, // Đảm bảo background là trong suốt để sử dụng background của modifier
                disabledTextColor = MaterialTheme.colors.onSurface,
                focusedIndicatorColor = Color.White, // Ẩn bottom border khi TextField được chọn
                unfocusedIndicatorColor = Color.White // Ẩn bottom border khi không được chọn
            ),
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