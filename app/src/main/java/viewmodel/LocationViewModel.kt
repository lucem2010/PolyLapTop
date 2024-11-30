package viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.District
import model.GHNService
import model.Province
import model.Ward

class LocationViewModel : ViewModel() {
    private val apiKey = "f006e2e3-a356-11ef-84ef-a2e10895a2aa"

    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces

    private val _districts = MutableStateFlow<List<District>>(emptyList())
    val districts: StateFlow<List<District>> = _districts

    private val _wards = MutableStateFlow<List<Ward>>(emptyList())
    val wards: StateFlow<List<Ward>> = _wards

    // Tạo các biến để lưu tên khi người dùng chọn
    private val _selectedProvinceName = MutableStateFlow<String?>(null)
    val selectedProvinceName: StateFlow<String?> = _selectedProvinceName

    private val _selectedDistrictName = MutableStateFlow<String?>(null)
    val selectedDistrictName: StateFlow<String?> = _selectedDistrictName

    private val _selectedWardName = MutableStateFlow<String?>(null)
    val selectedWardName: StateFlow<String?> = _selectedWardName

    init {
        fetchProvinces()
    }

    fun fetchProvinces() {
        viewModelScope.launch {
            try {
                val response = GHNService.api.getProvinces(apiKey)
                if (response.code == 200) {
                    _provinces.value = response.data
                } else {
                    // Xử lý lỗi khi không lấy được tỉnh/thành phố
                    Log.e("LocationViewModel", "Error fetching provinces: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Exception fetching provinces", e)
            }
        }
    }

    fun fetchDistricts(provinceId: Int) {
        viewModelScope.launch {
            try {
                val response = GHNService.api.getDistricts(apiKey,provinceId)
                if (response.code == 200) {
                    _districts.value = response.data
                } else {
                    // Xử lý lỗi khi không lấy được quận/huyện
                    Log.e("LocationViewModel", "Error fetching districts: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Exception fetching districts", e)
            }
        }
    }

    fun fetchWards(districtId: Int) {
        viewModelScope.launch {
            try {
                val response = GHNService.api.getWards(apiKey,districtId)
                if (response.code == 200) {
                    _wards.value = response.data
                } else {
                    // Xử lý lỗi khi không lấy được phường/xã
                    Log.e("LocationViewModel", "Error fetching wards: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Exception fetching wards", e)
            }
        }
    }

    fun selectProvince(province: Province) {
        _selectedProvinceName.value = province.ProvinceName
        _districts.value = emptyList() // Xóa dữ liệu quận/huyện cũ
        _wards.value = emptyList() // Xóa dữ liệu phường/xã cũ
        fetchDistricts(province.ProvinceID) // Lấy danh sách quận/huyện mới
    }

    fun selectDistrict(district: District) {
        _selectedDistrictName.value = district.DistrictName
        _wards.value = emptyList() // Xóa dữ liệu phường/xã cũ
        fetchWards(district.DistrictID) // Lấy danh sách phường/xã mới
    }

    fun selectWard(ward: Ward) {
        _selectedWardName.value = ward.WardName
    }
}