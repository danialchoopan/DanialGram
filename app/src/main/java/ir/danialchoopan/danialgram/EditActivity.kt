package ir.danialchoopan.danialgram

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import kotlinx.android.synthetic.main.activity_edit.*
import org.json.JSONObject

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val shareUser = getSharedPreferences("user", Context.MODE_PRIVATE)
        val post_id = intent.getIntExtra("post_id", 0)
        val post_text = intent.getStringExtra("post_text")
        txtPostDescriptionEdit.setText(post_text)
        btnUpdatePost.setOnClickListener {
            SingleTonRequestQueueVolley.instance(this@EditActivity)
                .add(object : StringRequest(Method.PUT,
                    EndPoints.GET_POSTS + "/${post_id}",
                    Response.Listener {
                        Log.d("response", it)
                        val jsonResult = JSONObject(it)
                        if (jsonResult.getBoolean("success")) {
                            Toast.makeText(
                                this@EditActivity,
                                "post updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditActivity, "post no update", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    Response.ErrorListener { }) {

                    override fun getParams(): MutableMap<String, String> {
                        val body = HashMap<String, String>()
                        body["text"] = txtPostDescriptionEdit.text.toString()
                        return body
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val token_access =
                            shareUser.getString("token", "");
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer $token_access";
                        return headers
                    }
                })
        }
    }
}