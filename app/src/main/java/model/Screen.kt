package model

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object BottomNav : Screen("bottom_nav")
    object Auth : Screen("auth")
    object DoiMatKhau : Screen("doiMatKhau")
    object DoiMatKhau1 : Screen("doiMatKhau1")
    object ThongTinCaNhan : Screen("ThongTinCaNhan")
    object ThongTinCaNhan1 : Screen("ThongTinCaNhan1")
    object ThongTinCaNhan2 : Screen("ThongTinCaNhan2")
    object ProductDetail : Screen("ProductDetail")
    object SearchScreen : Screen("SearchScreen")
    object OrderDetailsScreen : Screen("OrderDetailsScreen")
}