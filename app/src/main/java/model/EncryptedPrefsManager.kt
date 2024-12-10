package model
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import android.content.SharedPreferences
import com.google.gson.Gson


object SharedPrefsManager {
    // Khởi tạo SharedPreferences thay vì EncryptedSharedPreferences
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("my_secure_prefs", Context.MODE_PRIVATE)
    }

    // Lưu thông tin tài khoản và mật khẩu
    fun saveLoginInfo(
        context: Context,
        loggedInUser: User, // Đối tượng User
        token: String // Thêm tham số Token
    ) {
        val sharedPrefs = getSharedPreferences(context)
        val editor = sharedPrefs.edit()

        // Serialize the User object into a JSON string
        val userJson = Gson().toJson(loggedInUser)

        // Lưu userJson và token vào SharedPreferences
        editor.putString("user", userJson) // Lưu đối tượng User dưới dạng JSON
        editor.putString("token", token) // Lưu Token
        editor.apply() // Áp dụng thay đổi
    }

    private const val KEY_IS_DARK_THEME = "isDarkTheme"
    // Lưu trạng thái theme vào SharedPreferences
    fun saveThemeState(context: Context, isDarkTheme: Boolean) {
        val sharedPreferences = getSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_DARK_THEME, isDarkTheme)
            apply()
        }
    }

    // Lấy trạng thái theme từ SharedPreferences
    fun getThemeState(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(KEY_IS_DARK_THEME, false) // Mặc định là sáng
    }

    // Lấy thông tin tài khoản bao gồm userId, username, và password
    fun getLoginInfo(context: Context): Pair<User?, String?> {
        val sharedPrefs = getSharedPreferences(context)
        val userJson = sharedPrefs.getString("user", null)
        val token = sharedPrefs.getString("token", null)

        // Deserialize the User object from the JSON string
        val loggedInUser = if (userJson != null) {
            Gson().fromJson(userJson, User::class.java) // Chuyển JSON thành đối tượng User
        } else {
            null
        }

        return Pair(loggedInUser, token) // Trả về đối tượng User và Token
    }

    // Đăng xuất người dùng
    fun logoutUser(context: Context) {
        val sharedPrefs = getSharedPreferences(context)
        val editor = sharedPrefs.edit()

        // Xóa các thông tin liên quan đến người dùng và token
        editor.remove("user")  // Xóa thông tin đối tượng User được lưu dưới dạng JSON
        editor.remove("token") // Xóa Token
        editor.apply() // Áp dụng thay đổi
    }
}
