package viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.EncryptedPrefsManager
import model.User

class UserViewModel : ViewModel() {

    // Hàm đăng ký người dùng
    fun registerUser(user: User, onSuccess: (String, String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Use Dispatchers.IO for network operations
                withContext(Dispatchers.IO) {
                    val response = RetrofitClient.apiService.registerUser(user).execute()

                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        registerResponse?.let {
                            // Lấy AccessToken và RefreshToken từ phản hồi
                            val accessToken = it.AccessToken
                            val refreshToken = it.RefeshToken

                            // Gọi hàm onSuccess và truyền AccessToken đi
                            onSuccess("Đăng ký thành công!", accessToken)
                        }
                    } else {
                        onError("Lỗi: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("RegisterError", "Exception: ${e.message}")
                onError("Lỗi kết nối. Vui lòng thử lại!")
            }
        }
    }

    fun loginUser(username: String, password: String, onSuccess: (String, String, String, String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Tạo đối tượng User
                val user = User(username, password)

                // Gọi API đăng nhập
                val response = RetrofitClient.apiService.loginUser(user)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        // Lấy thông tin từ phản hồi
                        val accessToken = it.AccessToken
                        val refreshToken = it.RefeshToken
                        val userId = it._id
                        val role = it.Role

                        // Gọi hàm onSuccess và truyền các thông tin đăng nhập
                        onSuccess("Đăng nhập thành công!", accessToken, refreshToken, userId)
                    }
                } else {
                    onError("Lỗi: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LoginError", "Exception: ${e.message}")
                onError("Lỗi kết nối. Vui lòng thử lại!")
            }
        }
    }
}