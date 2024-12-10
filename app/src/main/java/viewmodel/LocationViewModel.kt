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

    // StateFlow để lưu danh sách tỉnh/thành phố, quận/huyện và phường/xã
    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces

    private val _districts = MutableStateFlow<List<District>>(emptyList())
    val districts: StateFlow<List<District>> = _districts

    private val _wards = MutableStateFlow<List<Ward>>(emptyList())
    val wards: StateFlow<List<Ward>> = _wards

    // Lưu trạng thái của tỉnh, quận/huyện, và phường/xã được chọn
    private val _selectedProvinceName = MutableStateFlow<String?>(null)
    val selectedProvinceName: StateFlow<String?> = _selectedProvinceName

    private val _selectedDistrictName = MutableStateFlow<String?>(null)
    val selectedDistrictName: StateFlow<String?> = _selectedDistrictName

    private val _selectedWardName = MutableStateFlow<String?>(null)
    val selectedWardName: StateFlow<String?> = _selectedWardName

    init {
        fetchProvinces()
    }

    // Lấy danh sách tỉnh/thành phố
    fun fetchProvinces() {
        viewModelScope.launch {
            try {
                val response = GHNService.api.getProvinces(apiKey)
                if (response.code == 200) {
                    _provinces.value = response.data
                    // Nếu đã có tỉnh được chọn trước đó, không cần chọn lại
                    if (_selectedProvinceName.value.isNullOrEmpty()) {
                        // Nếu chưa có tỉnh được chọn, chọn tỉnh đầu tiên
                        response.data.firstOrNull()?.let { firstProvince ->
                            _selectedProvinceName.value = firstProvince.ProvinceName
                            fetchDistricts(firstProvince.ProvinceID) // Lấy quận/huyện cho tỉnh đầu tiên
                        }
                    } else {
                        // Nếu đã có tỉnh được chọn, tìm tỉnh đã chọn trong danh sách
                        response.data.find { it.ProvinceName == _selectedProvinceName.value }
                            ?.let { selectedProvince ->
                                fetchDistricts(selectedProvince.ProvinceID) // Lấy quận/huyện cho tỉnh đã chọn
                            }
                    }
                } else {
                    // Xử lý lỗi khi không lấy được tỉnh/thành phố
                    Log.e("LocationViewModel", "Error fetching provinces: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Exception fetching provinces", e)
            }
        }
    }

    // Lấy danh sách quận/huyện theo tỉnh
    fun fetchDistricts(provinceId: Int) {
        viewModelScope.launch {
            try {
                val response = GHNService.api.getDistricts(apiKey, provinceId)
                if (response.code == 200) {
                    _districts.value = response.data
                    // Nếu quận/huyện không có dữ liệu, xóa đi
                    if (_selectedDistrictName.value.isNullOrEmpty()) {
                        // Nếu chưa có quận được chọn, chọn quận đầu tiên
                        response.data.firstOrNull()?.let { firstDistrict ->
                            _selectedDistrictName.value = firstDistrict.DistrictName
                            fetchWards(firstDistrict.DistrictID) // Lấy phường/xã cho quận đầu tiên
                        }
                    } else {
                        // Nếu quận đã được chọn, giữ nguyên và chỉ gọi lại phường/xã
                        response.data.find { it.DistrictName == _selectedDistrictName.value }
                            ?.let { selectedDistrict ->
                                fetchWards(selectedDistrict.DistrictID) // Lấy phường/xã cho quận đã chọn
                            } ?: run {
                            // Nếu quận đã chọn không có trong danh sách (ví dụ quận đã bị xóa), chọn quận đầu tiên
                            response.data.firstOrNull()?.let { firstDistrict ->
                                _selectedDistrictName.value = firstDistrict.DistrictName
                                fetchWards(firstDistrict.DistrictID) // Lấy phường/xã cho quận đầu tiên
                            }
                        }
                    }
                } else {
                    // Xử lý lỗi khi không lấy được quận/huyện
                    Log.e("LocationViewModel", "Error fetching districts: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Exception fetching districts", e)
            }
        }
    }

    // Lấy danh sách phường/xã theo quận/huyện
    fun fetchWards(districtId: Int) {
        viewModelScope.launch {
            try {
                val response = GHNService.api.getWards(apiKey, districtId)
                if (response.code == 200) {
                    _wards.value = response.data
                    // Nếu phường/xã không có dữ liệu, xóa đi
                    if (response.data.isEmpty()) {
                        _wards.value = emptyList()
                    } else {
                        val currentSelectedWard = _selectedWardName.value
                        // Sử dụng find để kiểm tra
                        if (response.data.find { it.WardName == currentSelectedWard } == null) {
                            // Nếu không tìm thấy, chọn ward mặc định
                            response.data.firstOrNull()?.let { firstWard ->
                                _selectedWardName.value = firstWard.WardName
                            }
                        }
                    }
                } else {
                    // Xử lý lỗi khi không lấy được phường/xã
                    Log.e("LocationViewModel", "Error fetching wards: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Exception fetching wards", e)
            }
        }
    }

    // Xử lý khi người dùng chọn tỉnh
    fun selectProvince(province: Province) {
        _selectedProvinceName.value = province.ProvinceName
        _districts.value = emptyList() // Xóa dữ liệu quận/huyện cũ
        _wards.value = emptyList() // Xóa dữ liệu phường/xã cũ
        fetchDistricts(province.ProvinceID) // Lấy danh sách quận/huyện mới
    }

    // Xử lý khi người dùng chọn quận/huyện
    fun selectDistrict(district: District) {
        _selectedDistrictName.value = district.DistrictName
        _wards.value = emptyList() // Xóa dữ liệu phường/xã cũ
        fetchWards(district.DistrictID) // Lấy danh sách phường/xã mới
    }

    // Xử lý khi người dùng chọn phường/xã
    fun selectWard(ward: Ward) {
        _selectedWardName.value = ward.WardName
    }

    // Cập nhật tên tỉnh đã chọn
    fun updateSelectedProvince(provinceName: String?) {
        _selectedProvinceName.value = provinceName

    }

    // Cập nhật tên quận/huyện đã chọn
    fun updateSelectedDistrict(districtName: String?) {
        _selectedDistrictName.value = districtName
    }

    // Cập nhật tên phường/xã đã chọn
    fun updateSelectedWard(wardName: String?) {
        _selectedWardName.value = wardName
    }
}