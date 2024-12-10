package bottomnavigation.ScreenBottomNavigation

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DanhgiaScreenBasic() {
    var selectedImagesUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current

    // Khai báo photoUri trong hàm này để có thể sử dụng nó khi chụp ảnh
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Chọn ảnh từ thư viện
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImagesUris = selectedImagesUris + uri
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    // Khởi tạo launcher để chụp ảnh
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            // Cập nhật lại danh sách ảnh khi ảnh được chụp
            photoUri?.let {
                selectedImagesUris = selectedImagesUris + it
                Log.d("TakePicture", "Photo was captured successfully")
            }
        } else {
            Log.d("TakePicture", "Photo capture failed or canceled")
        }
    }

    // Nội dung giao diện
    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Header(
            title = "Đánh Giá",
            onBackClick = { /* Xử lý khi nhấn nút back */ }
        )

        // Nội dung chính
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Hàng nút chức năng
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                ImagePickerButton(
                    text = "Thêm Hình Ảnh",
                    icon = Icons.Default.Photo,
                    onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                )

                Spacer(modifier = Modifier.width(16.dp))

                ImagePickerButton(
                    text = "Chụp Ảnh",
                    icon = Icons.Default.CameraAlt,
                    onClick = {
                        // Tạo URI mới mỗi khi nhấn nút chụp ảnh
                        val tempPhotoUri = createImageFileUri(context)
                        tempPhotoUri?.let {
                            photoUri = it
                            takePictureLauncher.launch(it)
                        }
                    }
                )
            }

            // Hiển thị ảnh đã chọn hoặc ảnh đã chụp
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()  // Đảm bảo LazyRow chiếm toàn bộ chiều rộng
                    .padding(8.dp)   // Thêm padding cho LazyRow
            ) {
                items(selectedImagesUris) { uri ->
                    Box(
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Gray)
                    ) {
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .width(150.dp)  // Đặt chiều rộng giới hạn cho ảnh
                                .height(150.dp) // Đặt chiều cao giới hạn cho ảnh
                                .clip(RoundedCornerShape(10.dp)),  // Màu sắc bo góc
                            contentScale = ContentScale.Crop  // Cắt ảnh cho vừa khung
                        )

                        // Nút "X" để xóa ảnh
                        IconButton(
                            onClick = {
                                selectedImagesUris = selectedImagesUris.filter { it != uri }
                            },
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Text input cho việc đánh giá
            TextInputField()

            Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa TextField và nút

            // Nút xác nhận
            ConfirmButton(
                onConfirmClick = {
                    // Xử lý khi nhấn nút xác nhận
                    println("Nút xác nhận đã được nhấn!")
                }
            )
        }
    }
}


fun createImageFileUri(context: Context): Uri? {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageFileName = "JPEG_${timestamp}_"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    var imageFile: File? = null
    try {
        imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return if (imageFile != null) {
        // Tạo URI cho file mà không cần sử dụng file_paths.xml
        FileProvider.getUriForFile(
            context,
            "com.yourapp.fileprovider", // Đảm bảo authority chính xác
            imageFile
        )
    } else {
        null
    }
}


@Composable
fun Header(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 30.dp, start = 20.dp, end = 20.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF8774A))
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 5.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImagePickerButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFF8774A),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = Color(0xFFF8774A),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TextInputField() {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .background(Color(0xFFF8F8F8)),
        contentAlignment = Alignment.TopStart
    ) {
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            placeholder = {
                Text(
                    text = "Hãy chia sẻ nhận xét cho sản phẩm này bạn nhé!",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start // Căn chỉnh trái
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start // Đảm bảo chữ nhập vào cũng căn trái
            )
        )
    }
}


@Composable
fun ConfirmButton(
    onConfirmClick: () -> Unit
) {
    Button(
        onClick = onConfirmClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF8774A))
    ) {
        Text(
            text = "Xác Nhận",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DanhgiaScreenBasicPreview() {
    DanhgiaScreenBasic()
}
