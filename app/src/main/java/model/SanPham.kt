package model

data class SanPham(
    val _id: String,
    val idHangSP: String,          // Tương ứng với `ObjectId` của Mongoose
    val tenSP: String,             // Tên sản phẩm
    val anhSP: List<String>? = null // Danh sách hình ảnh sản phẩm (có thể null)
)
