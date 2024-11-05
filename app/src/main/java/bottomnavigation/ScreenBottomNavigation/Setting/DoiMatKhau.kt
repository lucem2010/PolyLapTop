package bottomnavigation.ScreenBottomNavigation.Setting

import android.annotation.SuppressLint
import android.content.Intent
import android.view.ViewTreeObserver
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import bottomnavigation.ScreenBottomNavigation.SettingScreen
import com.example.polylaptop.R
import model.Screen

@Composable
fun DoiMatKhau(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffD9D9D9)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Header with Back Image and Title
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
        ConfirmationBox(navController = navController)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ConfirmationBox(navController: NavController) {
    var passwordValue by remember { mutableStateOf("") }
    val isButtonEnabled = derivedStateOf { passwordValue.length == 6 }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 70.dp)
            .size(300.dp)
            .background(Color(0xFFffffff), shape = RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, Color(0xFFD9D9D9)), shape = RoundedCornerShape(5.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Title
        Text(
            text = "Xác nhận mật khẩu",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, top = 20.dp)
        )

        // Instructions
        Text(
            text = "Quý khách vui lòng nhập lại mật khẩu để xác nhận đổi mật khẩu",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        // PIN Input
        Box(modifier = Modifier.padding(24.dp)) {
            PasswordInputField(
                passLength = 6,
                onPassChange = { pass ->
                    passwordValue = pass
                },
            )
        }
        Button(
            onClick = {
                navController.navigate(Screen.DoiMatKhau1.route)
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
                backgroundColor = if (isButtonEnabled.value) Color(0x809C7056) else Color(0xFFD9D9D9),
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
}

@Composable
fun PasswordInputField(passLength: Int, onPassChange: (String) -> Unit) {
    var passValue by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val keyboardState = keyboardAsState(KeyBoardStatus.Closed)
    val isShowWarning by remember(keyboardState) {
        derivedStateOf {
            if (keyboardState.value == KeyBoardStatus.Opened) {
                return@derivedStateOf passValue.length != passLength
            }
            return@derivedStateOf false
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember {
        FocusRequester()
    }


    // Password input field
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            })
        )

        // Eye icon to toggle visibility
        IconButton(
            modifier = Modifier.padding(top = 25.dp).fillMaxWidth(),
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
    }

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Composable
fun PassCell(
    char: String, isFocus: Boolean, isShowWarning: Boolean, modifier: Modifier = Modifier
) {
    val boderColor = if (isShowWarning) {
        MaterialTheme.colorScheme.error
    } else if (isFocus) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    Surface(
        modifier = modifier
            .size(15.dp)
            .aspectRatio(1f)
            .border(width = 2.dp, color = boderColor, shape = MaterialTheme.shapes.small),
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (char.isNotEmpty()) {
                Text(
                    text = char,  // Show dot instead of the actual character
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

enum class KeyBoardStatus {
    Opened,
    Closed
}

@Composable
fun keyboardAsState(inital: KeyBoardStatus = KeyBoardStatus.Closed): State<KeyBoardStatus> {
    val keyBoardState = remember { mutableStateOf(inital) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyBoardState.value = if (keypadHeight > screenHeight * 0.15) {
                KeyBoardStatus.Opened
            } else {
                KeyBoardStatus.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }
    return keyBoardState
}


@Composable
@Preview(showBackground = true)
fun DoiMatKhauPreview() {
    DoiMatKhau(navController = rememberNavController())
}



