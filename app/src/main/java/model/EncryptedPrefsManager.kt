package model
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import android.content.SharedPreferences


object EncryptedPrefsManager {
    // Khởi tạo EncryptedSharedPreferences
    // Khởi tạo EncryptedSharedPreferences
    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "my_secure_prefs", // Tên của SharedPreferences
            masterKeyAlias, // Khóa chính
            context, // Context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // Mã hóa khóa
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // Mã hóa giá trị
        )
    }


    // Lưu thông tin tài khoản và mật khẩu

    fun saveLoginInfo(context: Context, userId: String, username: String, password: String) {
        val sharedPrefs = getEncryptedSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.putString("userId", userId) // Lưu userId
        editor.putString("username", username) // Lưu tên đăng nhập
        editor.putString("password", password) // Lưu mật khẩu
        editor.apply() // Áp dụng thay đổi
    }


    // Kiểm tra nếu người dùng đã đăng nhập
    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPrefs = getEncryptedSharedPreferences(context)
        val username = sharedPrefs.getString("username", null) // Lấy tên đăng nhập
        return username != null // Nếu tên đăng nhập không rỗng, người dùng đã đăng nhập
    }

    // Lấy thông tin tài khoản bao gồm userId, username, và password
    fun getLoginInfo(context: Context): Triple<String?, String?, String?> {
        val sharedPrefs = getEncryptedSharedPreferences(context)
        val userId = sharedPrefs.getString("userId", null) // Lấy ID người dùng
        val username = sharedPrefs.getString("username", null) // Lấy tên đăng nhập
        val password = sharedPrefs.getString("password", null) // Lấy mật khẩu
        return Triple(userId, username, password)
    }

    // Đăng xuất người dùng
    fun logoutUser(context: Context) {
        val sharedPrefs = getEncryptedSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.remove("username") // Xóa tên đăng nhập
        editor.remove("password") // Xóa mật khẩu
        editor.apply() // Áp dụng thay đổi
    }
}