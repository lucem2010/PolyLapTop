package data

import model.ChiTietSanPham
import model.GioHang
import model.HangSP
import model.SanPham
import model.SanPhamResponse
import model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

    data class GioHangResponse(
        val message: String,          // Thông báo từ server
        val data: List<GioHang>       // Danh sách các giỏ hàng
    )

    data class AddToCartResponse(
        val message: String,  // Thông điệp từ server
        val data: GioHang?    // Dữ liệu về giỏ hàng (nếu có)
    )
    @GET("hang")
    suspend fun getHang(): Response<model.Response<HangSP>>  // Trả về SanPhamResponse

    @GET("chi-tiet-san-pham")
    suspend fun getChiTietSanPham(): Response<model.Response<ChiTietSanPham>>

    @GET("san-pham/")
    suspend fun getSanPham(): Response<SanPhamResponse>  // Trả về SanPhamResponse

    @GET("chi-tiet-san-pham/{idSanPham}")
    suspend fun getChiTietSanPham(
        @Path("idSanPham") idSanPham: String
    ): Response<ChiTietSanPhamResponse>


    // Lấy toàn bộ danh sách chi tiết sản phẩm
    @GET("chi-tiet-san-pham")
    suspend fun getAllChiTietSanPham(): Response<ChiTietSanPhamResponse>

    @POST("auth/register")
    fun registerUser(@Body user: User): Call<UserResponse>


    @POST("auth/login")
    suspend fun loginUser(@Body user: User): Response<UserResponse>


    @PUT("auth/user")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body userUpdate: User
    ): Response<Unit>


    @GET("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("gio-hang")
    suspend fun getCartItems(
        @Header("Authorization") token: String
    ): Response<GioHangResponse>



    @POST("gio-hang")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Response<ResponseBody>

    @DELETE("gio-hang/{cartId}")
    suspend fun deleteCart(
        @Header("Authorization") token: String,
        @Path("cartId") cartId: String  // Pass cartId as part of the URL path
    ): Response<ResponseBody>


    data class ChangePasswordRequest(
        val oldPassword: String,
        val newPassword: String
    )

    data class ApiResponse(
        val message: String,
        val data: UserData
    )

    data class UserData(
        val _id: String,
        val password: String,
    )

    @PUT("/auth/user/doi-mat-khau")
    suspend fun changePassword(
        @Header("Authorization") token: String, // Token Bearer cho xác thực
        @Body request: ChangePasswordRequest // Đối tượng chứa oldPassword và newPassword
    ): Response<ApiResponse>

    data class SanPhamCT (
        val idSanPhamCT: String,
        val SoLuongMua: Int
    )
    data class ThanhToanRequest (
        val Type: String,
        val SanPhamCTs: List<SanPhamCT>
    )
    data class DonHangResponse(
        val message: String,
        val data: Any?
    )

    @POST("/don-hang/mua-hang")
    suspend fun ThanhToan(
        @Header("Authorization") token: String,
        @Body request: ThanhToanRequest
    ): Response<DonHangResponse>



}