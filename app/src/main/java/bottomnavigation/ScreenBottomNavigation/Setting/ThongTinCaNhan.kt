package bottomnavigation.ScreenBottomNavigation.Setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.polylaptop.R
import model.Screen

@Composable
fun ThongTinCaNhan(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffD9D9D9)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(navController = navController)

        Spacer(modifier = Modifier.height(50.dp))

        ProfileCard(
            fullName = "Nguyễn Đức Hải",
            phoneNumber = "0123456789",
            birthDate = "22/01/2004",
            navController = navController
        )
    }
}

@Composable
fun Header(navController: NavController) {
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
}

@Composable
fun ProfileCard(
    fullName: String,
    phoneNumber: String,
    birthDate: String,
    navController: NavController
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 30.dp, end = 30.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
            .size(350.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img1),
            contentDescription = "Profile Image",
            modifier = Modifier
                .padding(top = 20.dp)
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(60.dp))

        //layoutHoVaTen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .clickable {
                    navController.navigate(Screen.ThongTinCaNhan1.route)
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(text = "Họ và tên", fontSize = 12.sp, color = Color.Black)
            Text(
                text = fullName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.right),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
        //layoutSoDienThoai
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Số điện thoại", fontSize = 12.sp, color = Color.Black)
            Text(
                text = phoneNumber,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 40.dp)
            )

        }
        //layoutNgaySinh
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .clickable {
                    navController.navigate("thongTinCaNhan2")
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Ngày sinh", fontSize = 12.sp, color = Color.Black)
            Text(
                text = birthDate,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.right),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ThongTinCaNhanPreview() {
    ThongTinCaNhan(navController = rememberNavController())
}