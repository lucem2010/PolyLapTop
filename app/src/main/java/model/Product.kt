package model

data class Product(
    val name: String,
    val description: String,
    val quantity: Int,
    val imageRes: Int,
    val status: OrderStatus
)

enum class OrderStatus {
    PROCESSING, SHIPPING, COMPLETED
}