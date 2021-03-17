package ir.danialchoopan.danialgram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferenceUser = getSharedPreferences("user", Context.MODE_PRIVATE)

        SingleTonRequestQueueVolley.instance(this@MainActivity).add(
            object : StringRequest(Request.Method.GET, EndPoints.CHECK_TOKEN,
                Response.Listener {
                    val jsonResult = JSONObject(it)
                    if (!jsonResult.getBoolean("success")) {
                        Intent(this@MainActivity, AuthActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                },
                Response.ErrorListener { }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val token_access = sharedPreferenceUser.getString("token", "");
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token_access";
                    return headers
                }

            }
        )
        Log.d("isUserLogin", sharedPreferenceUser.getBoolean("isUserLogin", false).toString())

        if (checkIfIsTheFirstTimeUserOpenTheApp()) {
            Intent(this@MainActivity, OnBoardActivity::class.java).also {
                startActivity(it)
            }
        }
        if (sharedPreferenceUser.getBoolean("isUserLogin", false)) {
            if (sharedPreferenceUser.getBoolean("isUserInfoCompiled", false)) {
                Intent(this@MainActivity, HomeActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(this@MainActivity, UserInfoActivity::class.java).also {
                    startActivity(it)
                }
            }
            finish()
        } else {
            Intent(this@MainActivity, AuthActivity::class.java).also {
                startActivity(it)
            }
        }
        //checkIfIsTheFirstTimeUserOpenTheApp


        finish()

        //CheckInternetConnection

        //endCheckInternetConnection
    }

    private fun checkIfIsTheFirstTimeUserOpenTheApp(): Boolean {
        val sharedPreferenceSetting = getSharedPreferences("setting", Context.MODE_PRIVATE)
        Log.d(
            "_firstTimeOpenApp",
            "${sharedPreferenceSetting.getBoolean("firstTimeOpenApp", true)}"
        )
        return if (sharedPreferenceSetting.getBoolean("firstTimeOpenApp", true)) {
            sharedPreferenceSetting.edit().apply {
                putBoolean("firstTimeOpenApp", false)
                apply()
            }
            true
        } else
            false
    }
}