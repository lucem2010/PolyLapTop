import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import model.EncryptedPrefsManager.getToken
import model.Screen
import viewmodel.UserViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun DoiMatKhau(navController: NavController, viewModel: UserViewModel = viewModel()) {
    var oldPassword by remember { mutableStateOf("") }// Mật khẩu cũ
    var newPassword by remember { mutableStateOf("") } // Mật khẩu mới
    var confirmNewPassword by remember { mutableStateOf("") } // Xác nhận mật khẩu mới
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    val changePasswordResponse by viewModel.changePasswordResponse.observeAsState()
    val error by viewModel.error.observeAsState()
    // Lắng nghe kết quả từ ViewModel

    // Xử lý khi đổi mật khẩu thành công
    changePasswordResponse?.let {
        isLoading = false
        Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
        viewModel.resetChangePasswordState() // Reset trạng thái sau khi xử lý
    }

    // Xử lý khi có lỗi
    error?.let {
        isLoading = false
        errorMessage = it // Hiển thị thông báo lỗi
        viewModel.resetErrorState() // Reset trạng thái lỗi sau khi xử lý
    }


    // Các trạng thái hiển thị lỗi
    var oldPasswordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val isButtonEnabled = derivedStateOf {
        oldPassword.isNotEmpty() &&
                newPassword.isNotEmpty() &&
                confirmNewPassword.isNotEmpty()
    }
    var isPasswordVisible by remember { mutableStateOf(false) } // Trạng thái hiển thị mật khẩu
    var isPasswordVisible1 by remember { mutableStateOf(false) } // Trạng thái hiển thị mật khẩu mới
    var isPasswordVisible2 by remember { mutableStateOf(false) } // Trạng thái hiển thị xác nhận mật khẩu mới

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header with Back Image and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
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
                text = "Đổi mật khẩu",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        // Xác nhận mật khẩu hiện tại
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red)
            }
            Text("Xác nhận bằng mật khẩu", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = oldPassword,
                onValueChange = {
                    oldPassword = it
                    oldPasswordError = when {
//                        oldPassword.length < 6 || oldPassword.length > 15 -> "Mật khẩu phải có từ 6 đến 15 ký tự"
                        oldPassword.length < 3 || oldPassword.length > 8 -> "Mật khẩu phải có từ 3 đến 8 ký tự"
                        oldPassword.any { char -> !char.isLetterOrDigit() } -> "Sai mật khẩu"
                        else -> ""
                    }
                },
                label = { Text("Mật khẩu") },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = oldPasswordError.isNotEmpty()
            )
            if (oldPasswordError.isNotEmpty()) {
                Text(
                    text = oldPasswordError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
//            TextButton(
//                onClick = { navController.navigate(Screen.QuenMatKhauScreen.route) },
//                modifier = Modifier.align(Alignment.End)
//            ) {
//                Text(
//                    "Quên mật khẩu",
//                    fontSize = 16.sp,
//                    color = Color.Gray,
//                    style = MaterialTheme.typography.bodySmall.copy(
//                        textDecoration = TextDecoration.Underline // Gạch chân
//                    )
//                )
//            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mật khẩu mới
            Text("Mật khẩu mới", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    confirmPasswordError = when {
//                        newPassword.length < 6 || newPassword.length > 15 -> "Mật khẩu phải có từ 6 đến 15 ký tự"
                        newPassword.length < 3 || newPassword.length > 8 -> "Mật khẩu phải có từ 3 đến 8 ký tự"
                        confirmNewPassword != newPassword -> "Mật khẩu không khớp"
                        else -> ""
                    }
                },
                label = { Text("Mật khẩu mới") },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible1 = !isPasswordVisible1 }) {
                        Icon(
                            imageVector = if (isPasswordVisible1) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
            )

            // Xác nhận mật khẩu mới
            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = {
                    confirmNewPassword = it
                    confirmPasswordError = when {
                        confirmNewPassword != newPassword -> "Mật khẩu không khớp"
//                        confirmNewPassword.length < 6 || confirmNewPassword.length > 15 -> "Mật khẩu phải có từ 6 đến 15 ký tự"
                        confirmNewPassword.length < 3 || confirmNewPassword.length > 8 -> "Mật khẩu phải có từ 3 đến 8 ký tự"
                        else -> ""
                    }
                },
                label = { Text("Xác nhận mật khẩu mới") },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible2 = !isPasswordVisible2 }) {
                        Icon(
                            imageVector = if (isPasswordVisible2) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible2) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmPasswordError.isNotEmpty()
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(
                    text = confirmPasswordError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Button Đổi mật khẩu
            Button(
                onClick = {
                    if (newPassword != confirmNewPassword) {
                        errorMessage = "Mật khẩu mới không khớp!"
                        return@Button
                    }
                    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                        errorMessage = "Vui lòng điền đầy đủ thông tin!"
                        return@Button
                    }

                    isLoading = true // Hiển thị loading trong nút
                    // Lấy token từ SharedPreferences
                    val token = getToken(context) // Hàm getToken để lấy token

                    // Kiểm tra nếu không có token
                    if (token.isNullOrEmpty()) {
                        errorMessage = "Token không hợp lệ hoặc hết hạn. Vui lòng đăng nhập lại."
                        isLoading = false
                        return@Button
                    }
                    // Gửi yêu cầu đổi mật khẩu
                    viewModel.changePassword(
                        token = "Bearer $token", // Thay thế <your-token> bằng token thực tế
                        oldPassword = oldPassword,
                        newPassword = newPassword,

                        )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (isButtonEnabled.value) Color(0xFFF8774A) else Color(0xFFD9D9D9),
                        shape = RoundedCornerShape(5.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isButtonEnabled.value) Color(0xFFF8774A) else Color(
                        0xFFD9D9D9
                    ),
                    contentColor = Color.White
                ),
                enabled = true // Logic kích hoạt button
            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(color = Color.White)
//                } else {
                Text(
                    "Đổi mật khẩu",
                    color = if (isButtonEnabled.value) Color.White else Color.Black,
                    fontSize = 16.sp
                )
//                }
            }
        }
    }
}



@Composable
@Preview(showBackground = true)
fun DoiMatKhauPreview() {
    DoiMatKhau(navController = rememberNavController())
}