package model

data class SanPham(
    val _id: String,
    val idHangSP: idHangSP,          // Tương ứng với `ObjectId` của Mongoose
    val tenSP: String,             // Tên sản phẩm
    val anhSP: List<String>? = null // Danh sách hình ảnh sản phẩm (có thể null)
)
data class idHangSP(
    val _id: String,
    val TenHang: String
)