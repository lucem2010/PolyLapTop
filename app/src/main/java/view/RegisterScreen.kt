package view

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import model.User
import viewmodel.UserViewModel

@Composable
fun RegisterScreen(userViewModel: UserViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    // Kiểm tra kết nối mạng
    val context = LocalContext.current
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    fun validateAndRegister() {
        // Kiểm tra kết nối mạng
        if (!isNetworkAvailable()) {
            errorMessage = "Không có kết nối internet"
            return
        }

        // Validate form input
        val usernameTrimmed = username.trim()
        val passwordTrimmed = password.trim()
        val confirmPasswordTrimmed = confirmPassword.trim()

        if (usernameTrimmed.isEmpty() || passwordTrimmed.isEmpty() || confirmPasswordTrimmed.isEmpty()) {
            errorMessage = "Thiếu thông tin"
            return
        }

        if (passwordTrimmed.length <= 5) {
            errorMessage = "Mật khẩu phải có ít nhất 6 ký tự."
            return
        }

        if (passwordTrimmed != confirmPasswordTrimmed) {
            errorMessage = "Mật khẩu không khớp"
            return
        }

        // Gọi API đăng ký người dùng
        val user = User(UserName = usernameTrimmed, Password = passwordTrimmed)

        userViewModel.registerUser(user, onSuccess = { message, accessToken ->
            errorMessage = ""  // Xóa thông báo lỗi nếu đăng ký thành công
            successMessage = message  // Lưu thông báo thành công
            Log.d("RegisterSuccess", "AccessToken: $accessToken") // Log thông tin token nếu cần
        }, onError = {
            errorMessage = it
            Log.e("RegisterError", "Đăng ký thất bại: $it") // Log lỗi ra logcat
        })
    }

    // Hiển thị Toast khi có thông báo thành công
    LaunchedEffect(successMessage) {
        if (successMessage.isNotEmpty()) {
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
        }
    }

    // Giao diện UI
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp), // Bóng đổ cho Card
        shape = RoundedCornerShape(8.dp) // Bo góc cho Card
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các phần tử
        ) {
            // Trường nhập Tên người dùng
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Tên người dùng") },
                modifier = Modifier.fillMaxWidth()
            )

            // Trường nhập Mật khẩu với Icon mắt nhắm/mở
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiển thị mật khẩu")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Trường nhập lại Mật khẩu với Icon mắt nhắm/mở
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Nhập lại mật khẩu") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiển thị mật khẩu")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Hiển thị thông báo lỗi
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Nút Đăng ký
            Button(
                onClick = { validateAndRegister() },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
            ) {
                Text("Đăng Ký")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
