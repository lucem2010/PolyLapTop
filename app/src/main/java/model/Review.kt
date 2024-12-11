package model

data class Review(
    val username: String,
    val date: String, // Example format: "2024-11-07"
    val rating: Int, // Number of stars (1 to 5)
    val comment: String
)