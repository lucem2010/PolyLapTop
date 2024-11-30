package data

import model.AppConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
<<<<<<< HEAD
    private const val BASE_URL = AppConfig.ipAddress

=======
    private const val BASE_URL = "http://192.168.1.100:5000/"
//     "http://192.168.16.104:5000"
>>>>>>> eba0876dece6d080d289b6006a0ebc6add4629c4
    fun getBaseUrl(): String {
        return BASE_URL
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
