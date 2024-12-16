package view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import model.Screen
import model.SharedPrefsManager
import viewmodel.UserViewModel

@Composable
fun LoginScreen(viewModel: UserViewModel = viewModel(), navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp), // Bóng đổ cho Card
        shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)// Bo góc cho Card
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các phần tử
        ) {
            // Hiển thị thông báo lỗi nếu có
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Trường nhập tên người dùng
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Tên người dùng") },
                modifier = Modifier.fillMaxWidth()
            )
            // Trường nhập mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiển thị mật khẩu"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                onClick = {
                    // Kiểm tra xem tên người dùng và mật khẩu có trống không
                    if (username.isEmpty()) {
                        errorMessage = "Vui lòng nhập tên người dùng."
                        return@Button
                    }
                    if (password.isEmpty()) {
                        errorMessage = "Vui lòng nhập mật khẩu."
                        return@Button
                    }

                    // Đặt lại thông báo lỗi nếu có
                    errorMessage = ""

                    // Bắt đầu đăng nhập và gọi hàm trong ViewModel
                    isLoading = true
                    viewModel.loginUser(
                        username, password,
                        onSuccess = { message, loggedInUser, accessToken, refreshToken ->
                            // Lưu thông tin đăng nhập vào EncryptedPrefs
                            SharedPrefsManager.saveLoginInfo(
                                context,
                                loggedInUser, // Save the entire User object
                                accessToken   // Save the access token
                            )

                            // Chuyển đến màn hình chính hoặc bất kỳ màn hình nào bạn muốn
                            navController.navigate(Screen.BottomNav.route) {
                                popUpTo("auth") { inclusive = true }
                            }

                            // Dừng trạng thái tải
                            isLoading = false
                        },
                        onError = { error ->
                            // Xử lý lỗi nếu đăng nhập thất bại
                            errorMessage = error
                            isLoading = false
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Đăng Nhập",
                        color = Color.White // Set text color to white
                    )
                }
            }

        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen()
//}