package model

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

//data class Province(
//    val ProvinceID: Int,
//    val ProvinceName: String
//)
//
//
//data class District(
//    val DistrictID: Int,
//    val DistrictName: String,
//    val ProvinceID: Int
//)
//data class Ward(
//    val WardCode: String,
//    val WardName: String,
//    val DistrictID: Int
//)
//
//data class ApiResponse<T>(
//    val code: Int,
//    val message: String,
//    val data: T
//)
//
//interface GHNApi {
//    @GET("master-data/province")
//    suspend fun getProvinces(
//        @Header("Token") apiKey: String
//    ): ApiResponse<List<Province>>
//
//    @GET("master-data/district")
//    suspend fun getDistricts(
//        @Header("Token") apiKey: String,
//        @Query("province_id") provinceId: Int
//    ): ApiResponse<List<District>>
//
//    @GET("master-data/ward")
//    suspend fun getWards(
//        @Header("Token") apiKey: String,
//        @Query("district_id") districtId: Int
//    ): ApiResponse<List<Ward>>
//}