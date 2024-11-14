package viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import model.ChiTietSanPham
import model.SanPham
import retrofit2.Response

class SanPhamViewModel : ViewModel() {

    private val _sanPhamList = MutableLiveData<List<SanPham>>()
    val sanPhamList: LiveData<List<SanPham>> get() = _sanPhamList

    private val _chiTietSanPhamMap = MutableLiveData<Map<String, List<ChiTietSanPham>>>()
    val chiTietSanPhamMap: LiveData<Map<String, List<ChiTietSanPham>>> get() = _chiTietSanPhamMap

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val apiService = RetrofitClient.apiService

    // Hàm lấy danh sách sản phẩm
    fun fetchSanPham() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getSanPham()
                if (response.isSuccessful) {
                    val sanPhamData = response.body()?.data
                    sanPhamData?.let {
                        _sanPhamList.postValue(it)

                        // Sau khi có danh sách sản phẩm, gọi API để lấy chi tiết sản phẩm tương ứng
                        fetchChiTietSanPhamForAll(it)
                    }

                } else {
                    _errorMessage.value = "Lỗi khi tải dữ liệu sản phẩm: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Đã có lỗi xảy ra: ${e.message}"
                Log.e("SanPhamViewModel", "Error fetching SanPham: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Hàm lấy chi tiết sản phẩm cho toàn bộ danh sách sản phẩm
    private fun fetchChiTietSanPhamForAll(sanPhamList: List<SanPham>) {
        viewModelScope.launch {
            val chiTietMap = mutableMapOf<String, List<ChiTietSanPham>>()

            sanPhamList.forEach { sanPham ->
                try {
                    // Gọi API để lấy thông tin chi tiết sản phẩm theo idSanPham
                    val response = apiService.getChiTietSanPham(sanPham._id ?: "")

                    if (response.isSuccessful && response.body() != null) {
                        val chiTietSanPhamResponse = response.body()
                        val chiTietSanPhamList = chiTietSanPhamResponse?.data ?: emptyList()

                        // Lưu danh sách chi tiết vào map với idSanPham làm key
                        chiTietMap[sanPham._id ?: ""] = chiTietSanPhamList

                        Log.d("SanPhamViewModel", "Fetched details for idSanPham ${sanPham._id}")
                    } else {
                        Log.e("SanPhamViewModel", "API error for idSanPham ${sanPham._id}: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("SanPhamViewModel", "Error fetching chi tiet for idSanPham ${sanPham._id}: ${e.message}")
                }
            }

            // Cập nhật dữ liệu vào LiveData
            _chiTietSanPhamMap.postValue(chiTietMap)
        }
    }
}



