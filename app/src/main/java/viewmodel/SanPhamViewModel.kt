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

    private val _allChiTietSanPhamMap = MutableLiveData<Map<String, List<ChiTietSanPham>>>()
    val allChiTietSanPhamMap: LiveData<Map<String, List<ChiTietSanPham>>> get() = _allChiTietSanPhamMap

    private val _chiTietSanPhamList = MutableLiveData<List<ChiTietSanPham>>()
    val chiTietSanPhamList: LiveData<List<ChiTietSanPham>> get() = _chiTietSanPhamList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _chiTietSanPhamMap = MutableLiveData<Map<String, List<ChiTietSanPham>>>()
    val chiTietSanPhamMap: LiveData<Map<String, List<ChiTietSanPham>>> get() = _chiTietSanPhamMap

    private val _chitietsanphamList = MutableLiveData<List<ChiTietSanPham>>()
    val chitietsanphamList: LiveData<List<ChiTietSanPham>> get() = _chitietsanphamList

    private val apiService = RetrofitClient.apiService

    fun fetchSanPham() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getSanPham()
                if (response.isSuccessful) {
                    val sanPhamData = response.body()?.data
                    sanPhamData?.let {
                        _sanPhamList.postValue(it)
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

    fun fetchAllChiTietSanPham() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllChiTietSanPham()
                if (response.isSuccessful && response.body() != null) {
                    val chiTietSanPhamResponse = response.body()
                    val chiTietSanPhamList = chiTietSanPhamResponse?.data ?: emptyList()
                    val chiTietSanPhamMap = chiTietSanPhamList.groupBy { it.idSanPham._id }
                    _allChiTietSanPhamMap.postValue(chiTietSanPhamMap)
                } else {
                    Log.e("SanPhamViewModel", "API error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("SanPhamViewModel", "Error fetching all chi tiet san pham: ${e.message}")
            }
        }
    }

    fun fetchChiTietSanPhamOfid(idSanPham: String) {
        _isLoading.value = true // Bắt đầu trạng thái tải
        viewModelScope.launch {
            try {
                val response = apiService.getChiTietSanPham(idSanPham)
                if (response.isSuccessful && response.body() != null) {
                    val chiTietSanPhamResponse = response.body()
                    val chiTietSanPhamList = chiTietSanPhamResponse?.data ?: emptyList()

                    // Log kết quả phản hồi
                    Log.d("SanPhamViewModel", "Fetched chi tiet san pham for id $idSanPham: $chiTietSanPhamList")

                    // Cập nhật dữ liệu vào LiveData danh sách
                    _chiTietSanPhamList.postValue(chiTietSanPhamList)
                } else {
                    val errorMessage = "Lỗi: ${response.code()} - ${response.message()}"
                    _errorMessage.postValue(errorMessage)
                    Log.e("SanPhamViewModel", errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Lỗi khi gọi API: ${e.message}"
                _errorMessage.postValue(errorMessage)
                Log.e("SanPhamViewModel", errorMessage, e)
            } finally {
                _isLoading.value = false // Kết thúc trạng thái tải
            }
        }
    }

    fun fetchChiTietSanPham() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getChiTietSanPham()
                if (response.isSuccessful) {
                    val chiTietSanPhamData = response.body()?.data
                    chiTietSanPhamData?.let {
                        _chitietsanphamList.postValue(it)

                        // Sau khi có danh sách sản phẩm, gọi API để lấy chi tiết sản phẩm tương ứng
//                        fetchChiTietSanPhamForAll(it)
                    }
                    //nhu này à bắc

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

                        Log.d("SanPha   mViewModel", "Fetched details for idSanPham ${sanPham._id}")
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


    fun getFirstChiTietList(): List<ChiTietSanPham> {
        return _allChiTietSanPhamMap.value?.mapNotNull { (_, chiTietList) ->
            chiTietList.firstOrNull() // Lấy phần tử đầu tiên nếu tồn tại
        } ?: emptyList() // Trả về danh sách rỗng nếu không có dữ liệu
    }

}