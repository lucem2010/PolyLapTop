package data
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrolfitZalop {
    private const val BASE_URL = "https://sandbox.zalopay.com.vn/v001/tpe/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}