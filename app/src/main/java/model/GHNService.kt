package model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GHNService {
    private const val BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/"

    val api: GHNApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GHNApi::class.java)
    }
}