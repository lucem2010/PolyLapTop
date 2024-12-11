package model

data class SanPhamResponse(
    val message: String,
    val data: List<SanPham>  // Chứa danh sách sản phẩm
)
