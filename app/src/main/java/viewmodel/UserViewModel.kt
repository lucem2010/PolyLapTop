package viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ApiService
import data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.User

class UserViewModel : ViewModel() {


    // LiveData để theo dõi trạng thái cập nhật
    private val _updateUserStatus = MutableLiveData<Result<Boolean>>()
    val updateUserStatus: LiveData<Result<Boolean>> get() = _updateUserStatus

    fun updateUser(token: String, user: User) {
        viewModelScope.launch {
            try {
                // Log thông tin đầu vào
                Log.d("UpdateUser", "Token: Bearer $token")
                Log.d("UpdateUser", "User Data: $user")

                // Gửi yêu cầu cập nhật người dùng
                val response = RetrofitClient.apiService.updateUser("Bearer $token", user)

                // Log phản hồi từ API
                Log.d("UpdateUser", "Response: ${response.raw()}")
                Log.d("UpdateUser", "Response Body: ${response.body()}")
                Log.d("UpdateUser", "Response Code: ${response.code()}")

                // Kiểm tra phản hồi từ API
                if (response.isSuccessful) {
                    // Nếu thành công, cập nhật LiveData
                    Log.d("UpdateUser", "Update Successful")
                    _updateUserStatus.postValue(Result.success(true))
                } else {
                    // Nếu không thành công, cập nhật LiveData với lỗi
                    val errorMessage = "Update failed: ${response.message()}"
                    Log.e("UpdateUser", errorMessage)
                    _updateUserStatus.postValue(Result.failure(Throwable(errorMessage)))
                }
            } catch (e: Exception) {
                // Log lỗi xảy ra
                Log.e("UpdateUser", "Exception: ${e.message}", e)

                // Xử lý ngoại lệ và cập nhật LiveData
                _updateUserStatus.postValue(Result.failure(e))
            }
        }
    }

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

    fun loginUser(
        username: String,
        password: String,
        onSuccess: (String, User, String, String) -> Unit, // Updated to include User and tokens
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Tạo đối tượng User
                val user = User(UserName = username, Password=password)

                // Gọi API đăng nhập
                val response = RetrofitClient.apiService.loginUser(user)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("LoginResponse", "Response Body: $loginResponse")
                    loginResponse?.let {
                        // Lấy thông tin từ phản hồi
                        val accessToken = it.AccessToken
                        val refreshToken = it.RefeshToken


                        // Tạo đối tượng User từ phản hồi API
                        val loggedInUser = User(
                            ID = it._id,
                            UserName = it.UserName,
                            Password = password, // User's password is passed here as well
                            HoTen = it.HoTen,
                            Tuoi = it.Tuoi?.toInt(), // Convert Tuoi to Int
                            Email = it.Email,
                            Sdt = it.Sdt,
                            Avatar = it.Avatar,
                            DiaChi = it.DiaChi,

                        )

                        // Gọi hàm onSuccess và truyền đối tượng User cùng với AccessToken và RefreshToken
                        onSuccess("Đăng nhập thành công!", loggedInUser, accessToken, refreshToken)
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



    // Function to logout
    fun logoutUser(
        token: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Gọi API logout
                val response = RetrofitClient.apiService.logout("Bearer $token")

                if (response.isSuccessful) {
                    // Khi logout thành công
                    onSuccess("Đăng xuất thành công!")
                } else {
                    // Lỗi khi gọi API
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        "Lỗi từ server: $errorBody"
                    } else {
                        "Lỗi không xác định, mã lỗi: ${response.code()}"
                    }
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                // Xử lý lỗi ngoại lệ
                Log.e("LogoutError", "Exception: ${e.message}", e)
                onError("Lỗi kết nối. Vui lòng thử lại!")
            }
        }
    }

    private val _changePasswordResponse = MutableLiveData<ApiService.ApiResponse>()
    val changePasswordResponse: LiveData<ApiService.ApiResponse> get() = _changePasswordResponse
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun changePassword(
        token: String,
        oldPassword: String,
        newPassword: String,
    ) {
        viewModelScope.launch {
            try {
                // Tạo request body
                val request = ApiService.ChangePasswordRequest(oldPassword, newPassword)

                // Gọi API thông qua repository
                val response = RetrofitClient.apiService.changePassword(token, request)

                if (response.isSuccessful) {
                    // Xử lý khi API trả về thành công
                    _changePasswordResponse.postValue(response.body())
                } else {
                    // Xử lý lỗi từ server
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _error.postValue("Failed: $errorMessage")
                }
            } catch (e: Exception) {
                // Xử lý lỗi ngoại lệ (network hoặc logic)
                _error.postValue("Exception: ${e.message}")
            }

        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun resetChangePasswordState() {
        _changePasswordResponse.postValue(null)
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun resetErrorState() {
        _error.postValue(null)
    }
    // Sử dụng MutableLiveData để quản lý trạng thái sáng/tối
    private val _isDarkTheme = MutableLiveData<Boolean>()

    // Lấy giá trị theme từ EncryptedPrefsManager khi ViewModel được khởi tạo
    val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme



}