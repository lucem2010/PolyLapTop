package data

import model.ChiTietSanPham
import model.SanPham
import model.SanPhamResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    data class ChiTietSanPhamResponse(
        val message: String,
        val data: List<ChiTietSanPham>
    )



    @GET("san-pham/")
    suspend fun getSanPham(): Response<SanPhamResponse>  // Trả về SanPhamResponse

    @GET("chi-tiet-san-pham/{idSanPham}")
    suspend fun getChiTietSanPham(
        @Path("idSanPham") idSanPham: String
    ): Response<ChiTietSanPhamResponse>




}