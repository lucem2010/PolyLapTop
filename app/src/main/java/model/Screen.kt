package model

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object BottomNav : Screen("bottom_nav")
    object Auth : Screen("auth")
    object DoiMatKhau : Screen("doiMatKhau")
    object QuenMatKhauScreen : Screen("QuenMatKhauScreen")
    object ThongTinCaNhan : Screen("ThongTinCaNhan")
    object ProductDetail : Screen("ProductDetail")
<<<<<<< HEAD
    object OrderDetailsScreen : Screen("OrderDetailsScreen")
    object SearchScreen : Screen("SearchScreen")
=======
    object LocationScreen : Screen("LocationScreen")
>>>>>>> eba0876dece6d080d289b6006a0ebc6add4629c4
}