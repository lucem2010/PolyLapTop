package model

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object BottomNav : Screen("bottom_nav")
    object Auth : Screen("auth")
    object DoiMatKhau : Screen("doiMatKhau")
    object DoiMatKhau1 : Screen("doiMatKhau1")
    object ThongTinCaNhan : Screen("ThongTinCaNhan")
    object ProductDetail : Screen("ProductDetail")
    object LocationScreen : Screen("LocationScreen")
}