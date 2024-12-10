package viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.ApiService
import data.RetrofitClient
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.launch
import model.AppConfig
import model.Message


class ChatViewModel : ViewModel() {
    private lateinit var socket: Socket
    private val apiService = RetrofitClient.apiService
    private val _chatId = mutableStateOf("") // Biến này sẽ lưu giá trị chuỗi
    val chatId: State<String> = _chatId // Chỉ cung cấp truy cập đọc cho UI
    fun updateText(newText: String) {
        _chatId.value = newText // Cập nhật giá trị
    }

    private val _chat = mutableStateOf("") // Biến này sẽ lưu giá trị chuỗi
    val chat: State<String> = _chat // Chỉ cung cấp truy cập đọc cho UI
    fun updateIdChat(newText: String) {
        _chat.value = newText // Cập nhật giá trị
    }

    private val _message = mutableStateOf<Message?>(null) // Lưu trữ tin nhắn duy nhất
    val message: State<Message?> = _message // Chỉ cung cấp truy cập đọc cho UI

    // Hàm cập nhật tin nhắn mới
    private fun addNewMessage(message: Message) {
        _message.value = message // Cập nhật tin nhắn mới
    }

    init {
        try {
            socket = IO.socket(AppConfig.ipAddress)
            socket.connect()
            // Lắng nghe sự kiện người dùng tham gia phòng chat
//        val chatId = ""
            socket.on("joinChat") { args ->
                if (args.isNotEmpty()) {
                    updateText(args[0] as String)
                    // xu ly su kien nguoi dung tham gia phong chat
                }
            }
            // lang nghe su kien tin nhan moi
            socket.on("new_message") { msg ->
                val jsonMessage = msg[0] as String
                val gson = Gson()
                try {
                    val message = gson.fromJson(jsonMessage, Message::class.java)
                    // Xử lý đối tượng messag
                    if (message?.chatId == chat.value) {
                        addNewMessage(message)
                    }

                } catch (e: Exception) {
                    Log.e("Socket", "Error parsing message", e)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Gửi yêu cầu tham gia phòng chat
    fun joinChat(chatId: String) {
        socket.emit("joinChat", chatId)
    }

    fun sendMessage(
        token: String,
        context: Context,
        chatId: String,
        content: String,

    ) {
        val gson = Gson()
        viewModelScope.launch {
            try {
                val sendMessageRequest =
                    ApiService.SendMessageRequest(chatId = chatId, content = content)
                val res = apiService.sendMessage(sendMessageRequest, "Bearer $token")
                if (res.isSuccessful) {
                    Toast.makeText(context, "${res.body()?.message}", Toast.LENGTH_SHORT).show()
                    val messageJson = gson.toJson(res.body()?.data)
                    socket.emit("new_message", messageJson)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun contactChat(token: String, context: Context, callback: (ApiService.ChatData?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.contactMessage("Bearer $token")
                if (response.isSuccessful) {
                    Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT)
                        .show()
                    val chatId = response.body()?.data?.chat?._id
                    joinChat(chatId.toString())
                    updateIdChat(chatId.toString())
                    callback(response.body()?.data)
                } else {
                    Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT)
                        .show()
                    callback(null)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }
}