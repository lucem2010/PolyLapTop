package bottomnavigation.ScreenBottomNavigation.Setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.polylaptop.R


@Composable
fun ThongTinCaNhan1(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffD9D9D9)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.left),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Text(
                text = "Thông tin cá nhân",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // UserName Input with state handling
            var username by remember { mutableStateOf("Nguyễn Đức Hải") }

            UserNameInput(
                username = username,
                onUsernameChange = { username = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Character Count Text
            Text(
                text = "14/50",
                modifier = Modifier.align(Alignment.End),
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Save Button
            Box(
                modifier = Modifier
                    .padding(top = 30.dp) // Equivalent to layout_marginTop
                    .background(
                        color = Color(0xFF809C7056), // Solid color
                        shape = RoundedCornerShape(5.dp) // Rounded corners
                    )
                    .border(
                        border = BorderStroke(2.dp, Color(0xFF9C7056)), // Stroke color
                        shape = RoundedCornerShape(5.dp) // Same shape as background
                    )
                    .clickable{

                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding inside the button
                    .align(Alignment.CenterHorizontally) // Center the button
            ) {
                Text(
                    text = "Lưu",
                    color = Color.White, // Text color
                    fontWeight = FontWeight.Bold, // Text style
                    style = TextStyle(fontSize = 18.sp), // Text size
                    textAlign = TextAlign.Center // Center text
                )
            }
        }
    }
}

@Composable
fun UserNameInput(
    username: String,
    onUsernameChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
    ) {
        TextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 16.sp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ThongTinCaNhan1Preview() {
    ThongTinCaNhan1(navController = rememberNavController())
}