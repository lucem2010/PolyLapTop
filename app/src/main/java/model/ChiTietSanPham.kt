package model

import kotlinx.serialization.Serializable

@Serializable
data class ChiTietSanPham(
    val _id: String,
    val idSanPham: SanPham,
    val MauSac: String,
    val Ram: String,
    val SSD: String,
    val ManHinh: String,
    val SoLuong: Int,
    val Gia: Long,
    val MoTa: String
)