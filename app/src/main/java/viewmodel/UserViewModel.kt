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
import com.google.firebase.auth.FirebaseAuth
import data.ApiService
import data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.SharedPrefsManager
import model.User
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class UserViewModel : ViewModel() {
    // LiveData để theo dõi trạng thái cập nhật
    private val _updateUserStatus = MutableLiveData<Result<Boolean>>()
    val updateUserStatus: LiveData<Result<Boolean>> get() = _updateUserStatus
    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> get() = _loggedInUser
    // Phương thức cập nhật người dùng
    fun updateUser(token: String, user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
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
                    Log.d("UpdateUser", "Update Successful")
                    _updateUserStatus.postValue(Result.success(true))
                    _loggedInUser.value = user // Cập nhật người dùng trong ViewModel
                    onSuccess()
                } else {
                    // Nếu không thành công, cập nhật LiveData với lỗi
                    val errorMessage = "Update failed: ${response.message()}"
                    Log.e("UpdateUser", errorMessage)
                    _updateUserStatus.postValue(Result.failure(Throwable(errorMessage)))
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                // Log lỗi xảy ra
                Log.e("UpdateUser", "Exception: ${e.message}", e)
                // Xử lý ngoại lệ và cập nhật LiveData
                _updateUserStatus.postValue(Result.failure(e))
                onError(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    // Phương thức để tải lên avatar
    fun uploadAvatar(
        token: String,
        avatarFile: File,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Log the start of the upload process
                Log.d("UploadAvatar", "Starting upload for file: ${avatarFile.name}")

                // Prepare the request body
                val requestBody = avatarFile.asRequestBody("image/*".toMediaTypeOrNull())
                val multipartFile = MultipartBody.Part.createFormData("avatar", avatarFile.name, requestBody)

                // Gửi yêu cầu upload
                val response = RetrofitClient.apiService.uploadAvatar("Bearer $token", multipartFile)

                // Kiểm tra phản hồi từ server
                if (response.isSuccessful) {
                    val avatarUrl = response.body()?.data
                    Log.d("UploadAvatar", "Upload success: $avatarUrl")

                    // Nếu có URL avatar mới
                    if (avatarUrl != null) {
                        SharedPrefsManager.saveAvatarUrl(context, avatarUrl)
                        onSuccess(avatarUrl)
                    } else {
                        Log.e("UploadAvatar", "Avatar URL is null.")
                        onError("Không lấy được URL ảnh.")
                    }
                } else {
                    // Nếu không thành công, log thêm thông tin lỗi
                    val error = response.errorBody()?.string() ?: "Lỗi không xác định"
                    Log.e("UploadAvatar", "Upload failed: $error")
                    onError("Lỗi từ server: $error")
                }
            } catch (e: Exception) {
                // Log and trigger error callback for exceptions
                Log.e("UploadAvatar", "Exception during upload: ${e.message}")
                onError(e.message ?: "Unknown exception")
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
                val user = User(username, password)

                // Gọi API đăng nhập
                val response = RetrofitClient.apiService.loginUser(user)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        // Lấy thông tin từ phản hồi
                        val accessToken = it.AccessToken
                        val refreshToken = it.RefeshToken


                        // Tạo đối tượng User từ phản hồi API
                        val loggedInUser = User(
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
                // Gọi API logout với token của người dùng
                val response = RetrofitClient.apiService.logout("Bearer $token")

                if (response.isSuccessful) {
                    // Xử lý khi logout thành công
                    onSuccess("Đăng xuất thành công!")
                } else {
                    // Lỗi khi đăng xuất
                    val errorResponse = response.errorBody()?.string()
                    onError("Lỗi: $errorResponse")
                }
            } catch (e: Exception) {
                // Xử lý khi có lỗi kết nối
                Log.e("LogoutError", "Exception: ${e.message}")
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

    fun sendPasswordResetEmail(email: String, onComplete: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Gửi email thành công
                    Log.d("PasswordReset", "Email đã được gửi thành công.")
                    onComplete(true)
                } else {
                    // Gửi email thất bại
                    Log.e("PasswordReset", "Lỗi gửi email: ${task.exception?.message}")
                    onComplete(false)
                }
            }
    }
}