package model

import kotlinx.serialization.Serializable

@Serializable
data class ChiTietSanPham(
    val _id: String,
    val idSanPham: String, // Kiểu String để lưu ObjectId từ MongoDB
    val MauSac: String,
    val Ram: String,
    val SSD: String,
    val ManHinh: String,
    val SoLuong: Int,
    val Gia: Int,
    val MoTa: String
)
