package view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import bottomnavigation.ScreenBottomNavigation.SettingScreen
import kotlinx.coroutines.delay
import model.Message

import model.SharedPrefsManager
import viewmodel.ChatViewModel
import viewmodel.UserViewModel


@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel,
    viewModelUser: UserViewModel
) {
    val context = LocalContext.current
    var messageInput by remember { mutableStateOf("") } // Trạng thái của trường nhập liệu
    val loginInfo = SharedPrefsManager.getLoginInfo(context)
    var chatId by remember { mutableStateOf("") }
    val user = loginInfo?.first
    val token = loginInfo?.second.toString()
    var messagesUi by remember {
        mutableStateOf<List<Message>>(emptyList())
    }
    val isDarkTheme by viewModelUser.isDarkTheme.observeAsState(false)
    val backgroundBorderColor = Color(0xFFF8774A)
    val backgroundColor = if (isDarkTheme) Color(0xff898989) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.LightGray else Color(0xFFF8774A)
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    val message by viewModel.message // Quan sát danh sách tin nhắn
    // LaunchedEffect để theo dõi khi tin nhắn mới được thêm vào
    LaunchedEffect(message) {
        message?.let {
            messagesUi = messagesUi + it // Thêm tin nhắn vào danh sách hiện tại
        }
    }
    LaunchedEffect(Unit) {

        viewModel.contactChat(token, context) { data ->
            messagesUi = data?.messages!!
            chatId = data?.chat?._id!!
        }

    }
    // LazyListState để điều khiển cuộn
    val listState = rememberLazyListState()

    LaunchedEffect(messagesUi) {
        if (messagesUi.isNotEmpty()) {
            delay(500)
            // Cuộn đến tin nhắn cuối cùng khi messagesUi thay đổi
            listState.animateScrollToItem(messagesUi.size - 1)
        }
    }
    val isAtBottom = listState.firstVisibleItemIndex == messagesUi.size - 1
    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            listState.animateScrollToItem(messagesUi.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Màu nền nhạt
    ) {
        // Header với nút Back
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(borderColor)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                    tint = iconColor,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 5.dp)
                )
            }
            Text(
                text = "Chat",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
        // Danh sách tin nhắn
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            state = listState // Gán trạng thái cuộn cho LazyColumn
        ) {
            items(messagesUi) { message ->
                ChatMessageItem(message = message, Role = user?.Role ?: "")
            }
        }
        // Trường nhập liệu và nút gửi tin nhắn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageInput,
                onValueChange = { messageInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nhập tin nhắn...", color = textColor) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (messageInput.isNotBlank()) {
                        // Gửi tin nhắn
                        viewModel.sendMessage(token, context, chatId = chatId, messageInput)
                        messageInput = "" // Xóa nội dung sau khi gửi
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundBorderColor)
            ) {
                Text("Gửi", color = textColor)
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: Message, Role: String) {
    val isCurrentUser = message.senderId.Role == Role // Thay bằng ID người dùng hiện tại
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (isCurrentUser) Color(0xFFDCF8C6) else Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.senderId.HoTen,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Text(
                text = message.content,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPreview() {
    ChatScreen(
        navController = rememberNavController(),
        viewModel = ChatViewModel(),
        viewModelUser = UserViewModel()
    )
}