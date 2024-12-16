package model

data class DonHangCT(
    val idDonHang: String, // ID của đơn hàng, tham chiếu đến đối tượng DonHang
    val idSanPhamCT: ChiTietSanPham, // ID của chi tiết sản phẩm, tham chiếu đến ChiTietSanPham
    val SoLuongMua: Int, // Số lượng mua, bắt buộc
    val TongTien: Double? = null // Tổng tiền, không bắt buộc (nullable)
)
