package model

data class DanhGia(
    val _id: String,
    val idUser: User,
    val idHoaDon: String,  // id là id đơn hàng
    val Diem: Int,
    val NoiDung: String,
    val HinhAnh: List<String>,
)
