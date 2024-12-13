package data

import DonHang
import model.ChiTietSanPham
import model.DanhGia
import model.DonHangCT
import model.GioHang
import model.HangSP
import model.SanPham
import model.SanPhamResponse
import model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    data class ChiTietSanPhamResponse(
        val message: String,
        val data: List<ChiTietSanPham>
    )


    // Define the response data class based on the structure you provided
    data class UserResponse(
        val _id: String,            // ID của người dùng
        val UserName: String,       // Tên đăng nhập của người dùng
        val HoTen: String,          // Họ tên của người dùng
        val Tuoi: String,           // Ngày sinh của người dùng
        val Email: String,          // Email của người dùng
        val Sdt: String,            // Số điện thoại của người dùng
        val Avatar: String,         // Đường dẫn tới avatar của người dùng
        val DiaChi: String,         // Địa chỉ của người dùng
        val Role: String,           // Vai trò của người dùng (ví dụ: 'admin', 'Khách hàng')
        val AccessToken: String,    // Access Token sau khi đăng nhập thành công
        val RefeshToken: String    // Refresh Token để làm mới Access Token
    )

    data class GioHangResponse(
        val message: String,          // Thông báo từ server
        val data: List<GioHang>       // Danh sách các giỏ hàng
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


    // Define the login API function
    @POST("auth/login")
    suspend fun loginUser(@Body user: User): Response<UserResponse>


    @PUT("auth/user")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body userUpdate: User
    ): Response<Unit>


    @PUT("auth/logout")
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


    data class DonHangResponse(
        val message: String,
        val data: List<DonHang>
    )

    data class ChiTietDonHangResponse(
        val message: String,
        val data: List<DonHangCT>
    )


    @GET("don-hang")
    suspend fun getDonHang(
        @Header("Authorization") token: String
    ): Response<DonHangResponse>


    @GET("chi-tiet-don-hang/{id}")
    suspend fun getChiTietDonHang(
        @Path("id") idDonHang: String
    ): Response<ChiTietDonHangResponse>


    data class DanhGiaResponse(
        val message: String,
        val data: List<DanhGia>
    )

    @GET("danh-gia/{productId}")
    suspend fun getDanhGiaByProductId(
        @Path("productId") productId: String
    ): Response<DanhGiaResponse>


    @POST("don-hang/huy/{id}")
    suspend fun huyDonHang(
        @Path("id") donHangId: String,
        @Header("Authorization") token: String
    ): Response<Any>


    @POST("danh-gia/{id}")
    suspend fun taoDanhGia(
        @Path("id") donHangId: String,
        @Header("Authorization") token: String,
        @Part("Diem") diem: RequestBody,
        @Part("NoiDung") noiDung: RequestBody,
        @Part hinhAnh: List<MultipartBody.Part> // Truyền danh sách MultipartBody.Part
    ): Response<Any>
}