package viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.ApiService
import data.RetrofitClient
import kotlinx.coroutines.launch
import model.ChiTietSanPham
import model.GioHang
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class CartViewModel : ViewModel()  {
    private val _cartItems = MutableLiveData<List<GioHang>>()
    val cartItems: LiveData<List<GioHang>> get() = _cartItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isItemAdded = MutableLiveData<Boolean>()
    val isItemAdded: LiveData<Boolean> get() = _isItemAdded

    private val _isItemRemoved = MutableLiveData<Boolean>()

    private val apiService = RetrofitClient.apiService
    fun fetchCartItems(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getCartItems("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    _cartItems.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = "Không thể tải giỏ hàng. Vui lòng thử lại sau."
                }
            } catch (e: HttpException) {
                _errorMessage.value = "Lỗi server: ${e.message()}"
            } catch (e: IOException) {
                _errorMessage.value = "Lỗi kết nối mạng. Vui lòng kiểm tra lại internet."
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun removeFromCart(token: String, cartId: String, context: Context) {
        viewModelScope.launch {
            // Set loading state to true and reset item removal state
            _isLoading.value = true
            _isItemRemoved.value = false

            try {
                // Log token and cartId before sending the request
                Log.d("RemoveFromCart", "Token: Bearer $token")
                Log.d("RemoveFromCart", "cartId: $cartId")

                // Send API request to delete the cart
                val response = apiService.deleteCart(
                    token = "Bearer $token",
                    cartId = cartId // Pass cartId as part of the URL path
                )

                // Log the response status and body
                Log.d("RemoveFromCart", "Response Code: ${response.code()}")
                val responseBodyString = response.body()?.string()
                Log.d("RemoveFromCart", "Response Body: $responseBodyString")

                if (response.isSuccessful && !responseBodyString.isNullOrEmpty()) {
                    // Handle successful response
                    val jsonResponse = JSONObject(responseBodyString)
                    val message = jsonResponse.getString("message")
                    fetchCartItems(token)
                    if (message == "Xóa giỏ hàng thành công") {
                        _isItemRemoved.value = true




                        // Show success message
                        Toast.makeText(context, "Giỏ hàng đã được xóa.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle server errors or unsuccessful responses
                    Log.e("RemoveFromCart", "Error Body: ${response.errorBody()?.string()}")
                    _errorMessage.value = "Không thể xóa giỏ hàng. Vui lòng thử lại sau."
                    Toast.makeText(context, "Không thể xóa giỏ hàng. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                // Handle HTTP errors
                Log.e("RemoveFromCart", "HttpException: ${e.message()}")
                _errorMessage.value = "Lỗi server: ${e.message()}"
                Toast.makeText(context, "Lỗi server: ${e.message()}", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                // Handle network connection errors
                Log.e("RemoveFromCart", "IOException: ${e.message}")
                _errorMessage.value = "Lỗi kết nối mạng. Vui lòng kiểm tra lại internet."
                Toast.makeText(context, "Lỗi kết nối mạng. Vui lòng kiểm tra lại internet.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Catch any other unexpected errors
                Log.e("RemoveFromCart", "Exception: ${e.message}")
                _errorMessage.value = "Có lỗi xảy ra. Vui lòng thử lại."
                Toast.makeText(context, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
            } finally {
                // Set loading state to false after the request is complete
                _isLoading.value = false
                Log.d("RemoveFromCart", "Request completed")
            }
        }
    }



    fun addToCart(token: String, idChiTietSP: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _isItemAdded.value = false
            try {
                // Log token và idChiTietSP trước khi gửi request
                Log.d("AddToCart", "Token: Bearer $token")
                Log.d("AddToCart", "idChiTietSP: $idChiTietSP")

                // Gọi API
                val response = apiService.addToCart(
                    token = "Bearer $token",
                    body = mapOf("idChiTietSP" to idChiTietSP) // Đảm bảo gửi đúng JSON object
                )

                // Log thông tin phản hồi từ server
                Log.d("AddToCart", "Response Code: ${response.code()}")
                val responseBodyString = response.body()?.string()
                Log.d("AddToCart", "Response Body: $responseBodyString")

                if (response.isSuccessful && !responseBodyString.isNullOrEmpty()) {
                    // Parse response body
                    val jsonResponse = JSONObject(responseBodyString)
                    val message = jsonResponse.getString("message")

                    if (message == "Thêm sản phẩm vào giỏ hàng thành công") {
                        _isItemAdded.value = true
                        // Hiển thị thông báo khi thêm thành công
                        Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                    } else if (message == "Sản phẩm này trong giỏ hàng bạn đã có") {
                        // Hiển thị thông báo khi sản phẩm đã có trong giỏ hàng
                        Toast.makeText(context, "Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("AddToCart", "Error Body: ${response.errorBody()?.string()}")
                    _errorMessage.value = "Không thể thêm sản phẩm vào giỏ hàng. Vui lòng thử lại sau."
                    // Hiển thị thông báo lỗi
                    Toast.makeText(context, "Không thể thêm sản phẩm vào giỏ hàng. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Log.e("AddToCart", "HttpException: ${e.message()}")
                _errorMessage.value = "Lỗi server: ${e.message()}"
                // Hiển thị thông báo lỗi server
                Toast.makeText(context, "Lỗi server: ${e.message()}", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Log.e("AddToCart", "IOException: ${e.message}")
                _errorMessage.value = "Lỗi kết nối mạng. Vui lòng kiểm tra lại internet."
                // Hiển thị thông báo lỗi kết nối mạng
                Toast.makeText(context, "Lỗi kết nối mạng. Vui lòng kiểm tra lại internet.", Toast.LENGTH_SHORT).show()
            } finally {
                _isLoading.value = false
                Log.d("AddToCart", "Request completed")
            }
        }
    }
    fun ThanhToan(
        token: String,
        Type: String,
        SanPhamCTs: List<ApiService.SanPhamCT>,
        context:Context,
        callBack: (Boolean?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = ApiService.ThanhToanRequest(Type = Type, SanPhamCTs = SanPhamCTs)
                val response = apiService.ThanhToan("Bearer $token", request)
//                val responseBodyString = response.body()?.toString()
                if (response.isSuccessful) {
                    Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    callBack(true)
                } else {
                    Toast.makeText(context,"${response.body()?.message}",Toast.LENGTH_SHORT).show()
                    callBack(false)
                }
            } catch (e: HttpException) {
                Log.e("AddToCart", "HttpException: ${e.message()}")
                _errorMessage.value = "Lỗi server: ${e.message()}"
                // Hiển thị thông báo lỗi server
                Toast.makeText(context, "Lỗi server: ${e.message()}", Toast.LENGTH_SHORT).show()
                callBack(false)
            } catch (e: IOException) {
                Log.e("AddToCart", "IOException: ${e.message}")
                _errorMessage.value = "Lỗi kết nối mạng. Vui lòng kiểm tra lại internet."
                // Hiển thị thông báo lỗi kết nối mạng
                Toast.makeText(context, "Lỗi kết nối mạng. Vui lòng kiểm tra lại internet.", Toast.LENGTH_SHORT).show()
                callBack(false)
            } finally {
                _isLoading.value = false
                Log.d("AddToCart", "Request completed")
                callBack(false)
            }
        }
    }

}