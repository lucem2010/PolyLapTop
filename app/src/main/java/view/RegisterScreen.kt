package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        elevation = 4.dp, // Bóng đổ cho Card
        shape = RoundedCornerShape(8.dp) // Bo góc cho Card
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các phần tử
        ) {
            TextField(
                value = "", // Giá trị của trường nhập liệu
                onValueChange = { /* Xử lý thay đổi giá trị */ },
                label = { Text("Tên người dùng") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = "", // Giá trị của trường nhập liệu
                onValueChange = { /* Xử lý thay đổi giá trị */ },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = "", // Giá trị của trường nhập liệu
                onValueChange = { /* Xử lý thay đổi giá trị */ },
                label = { Text("Mật khẩu") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation() // Hiển thị mật khẩu
            )
            Button(
                onClick = { /* Xử lý đăng ký */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đăng Ký")
            }
        }
    }
}