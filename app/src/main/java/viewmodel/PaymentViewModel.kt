package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.widget.Toast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.polylaptop.zalopay.Api.CreateOrder


import data.ApiService
import data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import org.json.JSONObject
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener


class PaymentViewModel: ViewModel() {

    private val apiService = RetrofitClient.apiService

    fun createOrder(amount: String, callback: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { // Sử dụng Dispatchers.IO cho tác vụ mạng
            try {
                val orderApi = CreateOrder()
                val data: JSONObject = orderApi.createOrder(amount)
                withContext(Dispatchers.Main) { // Chuyển kết quả về Main Thread để cập nhật giao diện
                    if (data.getString("returncode") == "1") {
                        callback(data.getString("zptranstoken"))
                    } else {
                        callback(null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }
    fun handleZaloPayResult(intent: Intent, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            ZaloPaySDK.getInstance().onResult(intent)
//            Log.d("handleZaloPayResult", "handleZaloPayResult: vao day")
            onSuccess()
        } catch (e: Exception) {
            onError("Lỗi xử lý kết quả: ${e.message}")
        }
    }
    fun thanhToanOffline(
        token: String,
        phuongThuc: String,
        danhSachSP: List<ApiService.SanPhamCT>,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = ApiService.ThanhToanRequest(Type = phuongThuc, SanPhamCTs = danhSachSP)
                val response = apiService.ThanhToan("Bearer $token", request)
                if (response.isSuccessful) {
                    Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context,"Lỗi: ${response.body()?.message}",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createOrderAndPay(
        amount: String,
        context: Context,
        onPaymentSuccess: () -> Unit,
        onPaymentError: (String) -> Unit
    ) {
        createOrder(amount) { tokenZalo ->
            if (tokenZalo == null) {
                onPaymentError("Không tạo được đơn hàng.")
                return@createOrder
            }

            ZaloPaySDK.getInstance().payOrder(context as Activity, tokenZalo, "demozpdk://app", object :
                PayOrderListener {
                override fun onPaymentSucceeded(p0: String?, p1: String?, p2: String?) {
//                    Log.d("Thanh cong", "onPaymentCanceled: $p0, $p2, $p1")
                    onPaymentSuccess()
                }

                override fun onPaymentCanceled(p0: String?, p1: String?) {
//                    Log.d("Huy", "onPaymentCanceled: huy zalo $p0, $p1")
                    onPaymentError("Thanh toán bị hủy.")
                }

                override fun onPaymentError(p0: ZaloPayError?, p1: String?, p2: String?) {
                    onPaymentError( "Lỗi không xác định. $p0")
                }
            })
        }
    }
}
