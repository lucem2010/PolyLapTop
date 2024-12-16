package bottomnavigation.ScreenBottomNavigation

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import model.Screen
import model.SharedPrefsManager
import viewmodel.DanhGiaViewModel
import viewmodel.SanPhamViewModel
import viewmodel.UserViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Boolean
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DanhgiaScreenBasic(
    mainNavController: NavController,
    viewModel: DanhGiaViewModel = viewModel(),
    viewModelUser: UserViewModel
) {
    val navBackStackEntry = mainNavController.currentBackStackEntry
    val encodedId = navBackStackEntry?.arguments?.getString("id") ?: ""
    val donHangId = Uri.decode(encodedId) // Giải mã giá trị

    val result by viewModel.result.observeAsState()
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    // Áp dụng các màu sắc dựa trên giao diện
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val textDialogColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val borderDialogColor = if (isDarkTheme) Color(0x99AcACAC) else Color.Gray
    val textQMKColor = if (isDarkTheme) Color(0x99AcACAC) else Color.Gray
    val iconColor = if (isDarkTheme) Color.White else Color.Black

    var selectedImagesUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current

    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImagesUris = selectedImagesUris + uri
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                photoUri?.let {
                    selectedImagesUris = selectedImagesUris + it
                    Log.d("TakePicture", "Photo was captured successfully")
                }
            } else {
                Log.d("TakePicture", "Photo capture failed or canceled")
            }
        }

    var selectedRating by remember { mutableStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }


    val (loggedInUser, token) = SharedPrefsManager.getLoginInfo(context)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {
        Header(
            title = "Đánh Giá",
            onBackClick = {
                mainNavController.popBackStack()
            },
            viewModelUser = viewModelUser
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                ImagePickerButton(
                    text = "Thêm Hình Ảnh",
                    icon = Icons.Default.Photo,
                    onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    viewModelUser = viewModelUser
                )

                Spacer(modifier = Modifier.width(16.dp))

                ImagePickerButton(
                    text = "Chụp Ảnh",
                    icon = Icons.Default.CameraAlt,
                    onClick = {
                        val tempPhotoUri = createImageFileUri(context)
                        tempPhotoUri?.let {
                            photoUri = it
                            takePictureLauncher.launch(it)
                        }
                    },
                    viewModelUser = viewModelUser
                )
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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
                                .width(150.dp)
                                .height(150.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )

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

            // ComboBox cho thang điểm
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Thang Điểm: $selectedRating")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    (1..5).forEach { rating ->
                        DropdownMenuItem(onClick = {
                            selectedRating = rating
                            expanded = false
                        }) {
                            Text("$rating")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextInputField(
                text = text,
                onTextChange = { newText -> text = newText } // Pass the text change handler
            )

            Spacer(modifier = Modifier.height(8.dp))

            LaunchedEffect(result) {
                result?.let {
                    if (it) {
                        Toast.makeText(context, "Đánh giá thành công!", Toast.LENGTH_SHORT).show()
                        mainNavController.popBackStack() // Quay lại màn hình trước đó                    } else {
                        Toast.makeText(context, "Đánh giá thất bại!", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.clearResult() // Đặt lại LiveData để tránh lặp thông báo
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            ConfirmButton(
                onConfirmClick = {
                    // Convert selected images URIs to File objects
                    val imageFiles = selectedImagesUris.mapNotNull { uri ->
                        uriToFile(context, uri)  // Chuyển đổi Uri thành File
                    }

                    // Call taoDanhGia với các tham số cần thiết
                    if (token != null) {
                        viewModel.taoDanhGia(
                            donHangId = donHangId,
                            idUser = loggedInUser?.ID ?: "",
                            token = token,
                            diem = selectedRating.toString(),
                            noiDung = text,
                            hinhAnhFiles = imageFiles
                        )
                        Log.d("IDDDD", "Selected URI: $loggedInUser")
                    }
                }
            )

        }
    }
}

fun uriToFile(context: Context, uri: Uri): File? {
    val fileName = "temp_image_${System.currentTimeMillis()}.jpg"
    val tempFile = File(context.cacheDir, fileName)
    try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val outputStream = FileOutputStream(tempFile)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    return tempFile
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
    onBackClick: () -> Unit,
    viewModelUser: UserViewModel,
) {
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 30.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(borderColor)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = "Back",
                tint = textColor,
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
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImagePickerButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    viewModelUser: UserViewModel
) {
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TextInputField(text: String, onTextChange: (String) -> Unit) {
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
                onTextChange(newText) // Call the passed callback
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
fun ParentComposable() {
    var text by remember { mutableStateOf("") }

    TextInputField(
        text = text,
        onTextChange = { newText -> text = newText } // Pass the text change handler
    )
}


@Composable
fun ConfirmButton(
    onConfirmClick: () -> Unit
) {
    Button(
        onClick = onConfirmClick,
        modifier = Modifier
            .fillMaxWidth()
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
    DanhgiaScreenBasic(
        viewModel = DanhGiaViewModel(),
        mainNavController = rememberNavController(),
        viewModelUser = UserViewModel()
    )
}
