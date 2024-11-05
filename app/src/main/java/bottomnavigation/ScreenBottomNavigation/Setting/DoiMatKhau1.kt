package bottomnavigation.ScreenBottomNavigation.Setting

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.polylaptop.R

@SuppressLint("UnrememberedMutableState")
@Composable
fun DoiMatKhau1(navController: NavController) {
    var passwordValue by remember { mutableStateOf("") }
    var rePasswordValue by remember { mutableStateOf("") }
    val isButtonEnabled =
        derivedStateOf { passwordValue.length == 6 && rePasswordValue.length == 6 && passwordValue == rePasswordValue }
    var isPasswordVisible by remember { mutableStateOf(false) }


    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Header with Back Image and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
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
                text = "Đổi mật khẩu",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 70.dp)
                .size(350.dp)
                .background(Color(0xFFffffff), shape = RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, Color(0xFFD9D9D9)), shape = RoundedCornerShape(5.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Nhập mật khẩu mới
            PasswordSection(
                "Mật khẩu mới",
                passLength = 6,
                onPassChange = {
                    passwordValue = it
                },
                isPasswordVisible = isPasswordVisible
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Nhập lại mật khẩu
            PasswordSection(
                "Nhập lại mật khẩu",
                passLength = 6,
                onPassChange = {
                    rePasswordValue = it
                },
                isPasswordVisible = isPasswordVisible
            )
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                onClick = { isPasswordVisible = !isPasswordVisible }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Align icon and text vertically
                    horizontalArrangement = Arrangement.spacedBy(4.dp) // Space between icon and text
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        ),
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )

                    // "Xem mật khẩu" Text label next to the icon
                    Text(
                        text = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiển thị mật khẩu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Confirmation Button
            Button(
                onClick = {
                    if (passwordValue != rePasswordValue) {
                        Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Mật khẩu đã khớp", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isButtonEnabled.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(50.dp)
                    .background(
                        if (isButtonEnabled.value) Color(0x809C7056) else Color(0xFFD9D9D9),
                        shape = RoundedCornerShape(5.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isButtonEnabled.value) Color(0x809C7056) else Color(
                        0xFFD9D9D9
                    ),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(
                    "Xác nhận",
                    color = if (isButtonEnabled.value) Color.White else Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

    }
}

@Composable
fun PasswordSection(
    label: String,
    passLength: Int,
    onPassChange: (String) -> Unit,
    isPasswordVisible: Boolean,
) {
    var passValue by remember {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 25.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(25.dp))

        // Password Input Field
        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            value = passValue,
            onValueChange = { value ->
                if (value.length <= passLength) {
                    passValue = value
                    onPassChange(passValue)
                }
            },
            decorationBox = {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(passLength) { index ->
                        val char = when {
                            index >= passValue.length -> ""
                            else -> passValue[index].toString()
                        }
                        val isFocus = index == passValue.length
                        PassCell(
                            char = if (!isPasswordVisible && char.isNotEmpty()) "•" else char,
                            isFocus = isFocus,
                            isShowWarning = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }), singleLine = true
        )
    }
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Composable
@Preview(showBackground = true)
fun DoiMatKhau1Preview() {
    DoiMatKhau1(navController = rememberNavController())
}