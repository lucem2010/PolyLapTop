import java.util.Date


// Định nghĩa data class DonHang
data class DonHang(
    val _id: String, // ID của đơn hàng
    val idKhachHang: String, // ID của khách hàng
    val idAdmin: String? = null, // ID của admin, có thể null
    val NgayDatHang: Date, // Ngày đặt hàng
    val TrangThai: String, // Trạng thái đơn hàng
    val Type: String, // Phương thức thanh toán
    val tongTien: Double? = null // Tổng tiền, có thể null
)
