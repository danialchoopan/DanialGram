package ir.danialchoopan.danialgram.api

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import org.json.JSONObject

class UserApi(val context: Context) {
    val shareUser = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    fun logout() {
        SingleTonRequestQueueVolley.instance(context)
            .add(object : StringRequest(Request.Method.GET, EndPoints.LOGOUT,
                Response.Listener {
                    val jsonResult = JSONObject(it)
                    if (jsonResult.getBoolean("success")) {
                        Toast.makeText(context, "logout", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "error logout", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val token_access = shareUser.getString("token", "");
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token_access";
                    return headers
                }
            })
    }
}