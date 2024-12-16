package model

data class Sender(
    val _id: String,
    val HoTen: String,
    val Role: String
)

data class Message(
    val _id: String,
    val chatId: String,
    val senderId: Sender,
    val content: String,
    val isRead: Boolean,
    val timestamp: String,
    val __v: Int
)

