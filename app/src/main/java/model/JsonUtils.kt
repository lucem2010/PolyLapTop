package model

import com.google.gson.Gson

// Hàm chuyển đối tượng thành chuỗi JSON
fun <T> toJson(obj: T): String {
    return Gson().toJson(obj)
}

// Hàm chuyển chuỗi JSON thành đối tượng
inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, T::class.java)
}