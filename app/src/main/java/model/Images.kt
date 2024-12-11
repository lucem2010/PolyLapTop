package model

import com.example.polylaptop.R

data class Images(
    val id: Int,
    val pic: Int,
)
val imagesItem = listOf(
    Images(
        id = 1,
        pic = R.drawable.pro1,
    ),
    Images(
        id = 2,
        pic = R.drawable.pro2,
    ),
    Images(
        id = 3,
        pic = R.drawable.pro3,
    ),
    Images(
        id = 4,
        pic = R.drawable.pro4,
    ),
)
