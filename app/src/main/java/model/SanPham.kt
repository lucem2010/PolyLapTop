package model

import kotlinx.serialization.Serializable

@Serializable
data class SanPham(
    val _id: String,
    val idHangSP: HangSP,
    val tenSP: String,             // Tên sản phẩm
    val anhSP: List<String>? = null // Danh sách hình ảnh sản phẩm (có thể null)
)
