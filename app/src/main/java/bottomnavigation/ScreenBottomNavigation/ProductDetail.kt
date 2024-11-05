package bottomnavigation.ScreenBottomNavigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.polylaptop.R
import model.Images
import model.imagesItem


@Composable
fun ProductDetail(navController: NavController) {
    var isFavoriteVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff)),
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
                text = "Sản phẩm 1",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(
                    id = R.drawable.heart1
                ),
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.macbook),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        BorderStroke(
                            2.dp,
                            Color(0x80FFFFFF)
                        ),
                        shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState),
            ) {
                imagesItem.forEachIndexed { index, image -> // Use imagesItem here
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                            .clickable {
                                // Handle item click
                            }
                            .clip(RoundedCornerShape(5.dp)),
                    ) {
                        Image(
                            painter = painterResource(id = image.pic), // Use image.pic
                            contentDescription = "Image ${image.id}", // Modify contentDescription
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .size(70.dp)
                                .border(
                                    BorderStroke(
                                        2.dp,
                                        Color(0xB3FFFFFF)
                                    ),
                                    shape = RoundedCornerShape(5.dp) // Đảm bảo viền có cùng bo góc
                                ),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        Column {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
            ) {
                Text(
                    text = "Sản phẩm 1",
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "14.000.000 VND",
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Right
                )
            }
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .width(70.dp)
                        .height(30.dp)
                        .border(
                            BorderStroke(
                                1.dp,
                                Color(0xB3ACACAC)
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.star
                        ),
                        contentDescription = "star",
                        modifier = Modifier
                            .size(25.dp),
                        tint = Color.Yellow
                    )
                    Text(
                        text = "4.7",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "( 125+ Review )",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                )
            }
            Text(
                text = "Chọn loại",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 10.dp, start = 20.dp)
            )
            Row(modifier = Modifier.padding(top = 10.dp, start = 20.dp)) {
                TextChonLoai(label = "core i3")
                Spacer(modifier = Modifier.width(20.dp)) // Add spacing between items
                TextChonLoai(label = "core i5")
                Spacer(modifier = Modifier.width(20.dp)) // Add spacing between items
                TextChonLoai(label = "core i7")
            }
            Text(
                text = "Chọn màu",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 10.dp, start = 20.dp)
            )
            Row(
                modifier = Modifier.padding(top = 10.dp, start = 20.dp)
            ) {
                TextChonMau(color = 0xFF000000.toInt()) // Black color
                Spacer(modifier = Modifier.width(20.dp)) // Add spacing between items
                TextChonMau(color = 0xFFFF0000.toInt()) // Red color
            }
            //description
            Text(
                text = "Viverra auctor porta quam malesuada eu molestie dolor " +
                        "diam et. Tempor ipsum euismod nam luctus sit. Accu au" +
                        "ctor in mauris amet sit nibh. Mauris leo amet urna ullam" +
                        "corper. Viverra odio at risus phasellus egestas.Morbi sus",
                fontSize = 12.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Justify,
                color = Color(0xB3000000),
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1f)
            )
            Button(
                onClick = {
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                    .height(50.dp)
                    .background(
                        Color(0xFFD9D9D9),
                        shape = RoundedCornerShape(5.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0x809C7056),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(
                    "Add to card",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TextChonLoai(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp)) // Clip first to apply rounded corners
            .border(
                width = 2.dp, // Border thickness
                color = Color.Gray, // Border color
                shape = RoundedCornerShape(5.dp) // Border radius
            )
            .background(Color.LightGray) // Background after clipping
            .padding(5.dp) // Padding inside the box
    ) {
        Text(
            text = label,
            fontSize = 16.sp, // Font size
            fontWeight = FontWeight.Normal, // Font weight
            color = Color.Black,
        )
    }
}
@Composable
fun TextChonMau(color: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp)) // Clip to apply rounded corners
            .background(Color(color)) // Set the background color to the passed color
            .size(width = 50.dp, height = 20.dp) // Specify a size for the color box
    )
}

@Composable
@Preview(showBackground = true)
private fun ProductDetailPreview() {
    ProductDetail(navController = rememberNavController())
}