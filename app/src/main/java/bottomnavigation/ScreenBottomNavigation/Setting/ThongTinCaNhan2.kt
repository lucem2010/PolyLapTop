package bottomnavigation.ScreenBottomNavigation.Setting

import android.widget.DatePicker
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import java.util.Calendar

@Composable
fun ThongTinCaNhan2(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffD9D9D9)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Header Row
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
        // Initialize calendar and state
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // State for the selected date
        val date = remember { mutableStateOf("22/01/2004") }

        // DatePickerDialog
        val datePickerDialog = remember {
            android.app.DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    date.value = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                },
                year,
                month,
                day
            )
        }

        // Row for Date Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Date TextField
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            ) {
                TextField(
                    value = date.value,
                    onValueChange = { newValue -> date.value = newValue },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                // Calendar Icon Button
                IconButton(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendar",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            }
        }

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


@Composable
@Preview(showBackground = true)
fun ThongTinCaNhan2Preview() {
    ThongTinCaNhan2(navController = rememberNavController())
}