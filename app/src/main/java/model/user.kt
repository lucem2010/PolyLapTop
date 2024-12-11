package model
data class User(
    val UserName: String,
    val Password: String,
    val HoTen: String? = null,     // Tùy chọn, có thể null khi đăng ký lần đầu
    val Tuoi: Int? = null,         // Tùy chọn
    val Email: String? = null,     // Tùy chọn
    val Sdt: String? = null,       // Tùy chọn
    val Avatar: String? = null,    // Tùy chọn
    val DiaChi: String? = null,    // Tùy chọn
    val Role: String = "Khách hàng" // Vai trò mặc định là "Khách hàng"
)