package model

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object BottomNav : Screen("bottom_nav")
    object Auth : Screen("auth")
    object DoiMatKhau : Screen("doiMatKhau")
    object QuenMatKhauScreen : Screen("QuenMatKhauScreen")
    object ThongTinCaNhan : Screen("ThongTinCaNhan")
    object ProductDetail : Screen("ProductDetail")
    object LocationScreen : Screen("LocationScreen")
}