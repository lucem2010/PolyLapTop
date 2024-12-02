package model

import java.text.NumberFormat
import java.util.Locale

class Format {
    fun formatPrice(price: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return "${formatter.format(price)} đ"
    }
}