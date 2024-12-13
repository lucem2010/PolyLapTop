package viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RetrofitClient
import kotlinx.coroutines.launch
import model.DanhGia
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DanhGiaViewModel : ViewModel() {
    private val _danhGias = MutableLiveData<List<DanhGia>>()
    val danhGias: LiveData<List<DanhGia>> get() = _danhGias

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private val apiService = RetrofitClient.apiService

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> get() = _result

    fun fetchDanhGias(productId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getDanhGiaByProductId(productId)
                if (response.isSuccessful) {
                    _danhGias.value = response.body()?.data
                } else {
                    _errorMessage.value = "Error: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }
    fun createMultipartBodyList(files: List<File>): List<MultipartBody.Part> {
        return files.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("HinhAnh", file.name, requestFile)
        }
    }

    fun createRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }


    fun taoDanhGia(
        donHangId: String,
        idUser: String,
        token: String,
        diem: String,
        noiDung: String,
        hinhAnhFiles: List<File>
    ){
        viewModelScope.launch {
            try {
                val idUserBody = createRequestBody(idUser)
                val diemBody = createRequestBody(diem)
                val noiDungBody = createRequestBody(noiDung)
                val hinhAnhParts = createMultipartBodyList(hinhAnhFiles)

                val response = apiService.taoDanhGia(
                    donHangId = donHangId,
                    idUser = idUserBody,
                    token = "Bearer $token",
                    diem = diemBody,
                    noiDung = noiDungBody,
                    hinhAnh = hinhAnhParts
                )

                if (response.isSuccessful) {
                    _result.postValue(true)
                    Log.d("TaoDanhGia", "Đánh giá thành công")
                } else {
                    _result.postValue(false)
                    Log.e("TaoDanhGia", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _result.postValue(false)
                Log.e("TaoDanhGia", "Lỗi: ${e.message}")
            }
        }
    }


}