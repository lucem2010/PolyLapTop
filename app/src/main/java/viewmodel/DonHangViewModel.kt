package viewmodel

import DonHang
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ApiService
import data.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import model.DonHangCT
import retrofit2.Response

class DonHangViewModel : ViewModel() {
    // LiveData để lưu danh sách đơn hàng
    private val _donHangLiveData = MutableLiveData<List<DonHang>>()
    val donHangLiveData: LiveData<List<DonHang>> get() = _donHangLiveData

    // LiveData để lưu danh sách chi tiết đơn hàng
    private val _chiTietDonHangLiveData = MutableLiveData<List<DonHangCT>>()
    val chiTietDonHangLiveData: LiveData<List<DonHangCT>> get() = _chiTietDonHangLiveData

    // LiveData để lưu trạng thái lỗi (nếu có)
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val apiService = RetrofitClient.apiService

    // Hàm gọi API lấy danh sách đơn hàng
    fun getDonHang(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getDonHang("Bearer $token")
                if (response.isSuccessful) {
                    val donHangResponse = response.body()
                    val donHangList = donHangResponse?.data ?: emptyList()
                    _donHangLiveData.postValue(donHangList)

                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _errorLiveData.postValue(errorMessage)
                }
            } catch (e: Exception) {
                _errorLiveData.postValue(e.message ?: "An exception occurred")
            }
        }
    }

    // Hàm gọi API lấy danh sách chi tiết đơn hàng cho từng đơn hàng
     fun getChiTietDonHang(donHangId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getChiTietDonHang(donHangId)
                if (response.isSuccessful) {
                    val chiTietResponse = response.body()
                    _chiTietDonHangLiveData.postValue(chiTietResponse?.data ?: emptyList())
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _errorLiveData.postValue("Error for order $donHangId: $errorMessage")
                }
            } catch (e: Exception) {
                _errorLiveData.postValue(e.message ?: "An exception occurred")
            }
        }
    }

    private val _huyDonHangResult = MutableLiveData<Result<String>>()
    val huyDonHangResult: LiveData<Result<String>> get() = _huyDonHangResult

    fun huyDonHang(donHangId: String, token: String) {
        viewModelScope.launch {
            try {
                // Gọi API để hủy đơn hàng với token
                val response = apiService.huyDonHang(donHangId, "Bearer $token")

                if (response.isSuccessful) {
                    // Xử lý nếu API trả về thành công
                    _huyDonHangResult.value = Result.success("Đơn hàng đã được hủy thành công!")
                } else {
                    // Xử lý nếu có lỗi từ phía server
                    _huyDonHangResult.value = Result.failure(
                        Exception("Hủy đơn hàng thất bại: ${response.errorBody()?.string()}")
                    )
                }
            } catch (e: Exception) {
                // Xử lý nếu có lỗi từ phía client (kết nối, cú pháp, v.v.)
                _huyDonHangResult.value = Result.failure(
                    Exception("Lỗi khi gọi API: ${e.message}")
                )
            }
        }
    }



}


