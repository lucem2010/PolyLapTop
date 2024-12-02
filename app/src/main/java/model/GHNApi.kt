package model

import model.District
import model.Province
import model.Ward
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface GHNApi {
    @GET("master-data/province")
    suspend fun getProvinces(
        @Header("Token") apiKey: String
    ): ApiResponse<List<Province>>

    @GET("master-data/district")
    suspend fun getDistricts(
        @Header("Token") apiKey: String,
        @Query("province_id") provinceId: Int
    ): ApiResponse<List<District>>

    @GET("master-data/ward")
    suspend fun getWards(
        @Header("Token") apiKey: String,
        @Query("district_id") districtId: Int
    ): ApiResponse<List<Ward>>
}