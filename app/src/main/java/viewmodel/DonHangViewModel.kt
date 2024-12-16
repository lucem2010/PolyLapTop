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

    private val _huyDonHangResult = MutableLiveData<Result<String>?>()
    val huyDonHangResult: LiveData<Result<String>?> get() = _huyDonHangResult

    fun huyDonHang(donHangId: String, token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.huyDonHang(donHangId, "Bearer $token")
                if (response.isSuccessful) {
                    _huyDonHangResult.postValue(Result.success("Đơn hàng đã được hủy thành công!"))

                    // Gọi lại API để cập nhật danh sách đơn hàng
                    getDonHangList(token)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Lỗi không xác định"
                    _huyDonHangResult.postValue(Result.failure(Exception("Hủy đơn hàng thất bại: $errorMessage")))
                }
            } catch (e: Exception) {
                _huyDonHangResult.postValue(Result.failure(Exception("Lỗi khi gọi API: ${e.message}")))
            }
        }
    }

    // Hàm để reset trạng thái sau khi hoàn thành xử lý
    fun resetHuyDonHangResult() {
        _huyDonHangResult.postValue(null)
    }




    fun getDonHangList(token: String) {
        viewModelScope.launch {
            try {
                // Gọi API để lấy danh sách đơn hàng
                val response = apiService.getDonHang("Bearer $token")
                if (response.isSuccessful) {
                    val donHangResponse = response.body()
                    val donHangList = donHangResponse?.data ?: emptyList()

                    // Cập nhật danh sách đơn hàng vào LiveData
                    _donHangLiveData.postValue(donHangList)
                } else {
                    // Xử lý lỗi từ phía server
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _errorLiveData.postValue("Lỗi khi lấy danh sách đơn hàng: $errorMessage")
                }
            } catch (e: Exception) {
                // Xử lý lỗi từ phía client (kết nối, cú pháp, v.v.)
                _errorLiveData.postValue("Lỗi khi gọi API: ${e.message}")
            }
        }
    }


}


