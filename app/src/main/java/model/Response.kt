package model

data class Response<T>(
    val message: String,
    val data: List<T>  // Chứa danh sách sản phẩm
)
