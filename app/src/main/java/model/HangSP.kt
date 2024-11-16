package model

import kotlinx.serialization.Serializable

@Serializable
data class HangSP(
    val _id: String,  // ID của hãng sản phẩm
    val TenHang: String  // Tên của hãng sản phẩm
)
