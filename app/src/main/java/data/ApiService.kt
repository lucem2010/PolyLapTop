package data

import model.ChiTietSanPham
import model.SanPham
import model.SanPhamResponse
import model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    data class ChiTietSanPhamResponse(
        val message: String,
        val data: List<ChiTietSanPham>
    )


    data class UserResponse(
        val _id: String,            // ID của người dùng
        val UserName: String,       // Tên đăng nhập của người dùng
        val Role: String,           // Vai trò của người dùng (ví dụ: 'admin', 'Khách hàng')
        val AccessToken: String,    // Access Token sau khi đăng ký thành công
        val RefeshToken: String    // Refresh Token để làm mới Access Token
    )


    @GET("san-pham/")
    suspend fun getSanPham(): Response<SanPhamResponse>  // Trả về SanPhamResponse

    @GET("chi-tiet-san-pham/{idSanPham}")
    suspend fun getChiTietSanPham(
        @Path("idSanPham") idSanPham: String
    ): Response<ChiTietSanPhamResponse>


    @POST("auth/register")
    fun registerUser(@Body user: User): Call<UserResponse>


    @POST("auth/login")
    suspend fun loginUser(@Body user: User): Response<UserResponse>


}