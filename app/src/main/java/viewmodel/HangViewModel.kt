package viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RetrofitClient
import kotlinx.coroutines.launch
import model.ChiTietSanPham
import model.HangSP
import model.SanPham

class HangViewModel:ViewModel() {
    private val _hangList = MutableLiveData<List<HangSP>>()
    val hangList: LiveData<List<HangSP>> get() = _hangList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val apiService = RetrofitClient.apiService

    // Hàm lấy danh sách sản phẩm
    fun fetchHang() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getHang()
                if (response.isSuccessful) {
                    val HangSPData = response.body()?.data
                    HangSPData?.let {
                        _hangList.postValue(it)

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
}