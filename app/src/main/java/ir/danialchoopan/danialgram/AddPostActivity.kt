package ir.danialchoopan.danialgram

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_user_info.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class AddPostActivity : AppCompatActivity() {
    lateinit var img_upload: Bitmap
    val CODE_CHANGE_IMAGE = 113

    lateinit var userSharePreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        val img_uri_intent = intent.data
        userSharePreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        imgPostImage.setImageURI(img_uri_intent)
        img_upload = MediaStore.Images.Media.getBitmap(contentResolver, img_uri_intent)

        tvChoiceImage.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                startActivityForResult(it, CODE_CHANGE_IMAGE)
            }
        }
        btnSendPost.setOnClickListener {
            if (txtPostDescription.text.toString().isNotEmpty()) {
                sendPost()
            }
        }

    }

    private fun sendPost() {
        val progress_dialog = ProgressDialog(this@AddPostActivity)
        progress_dialog.setTitle("sending data")
        progress_dialog.show()
        val strRequestSendPost = object : StringRequest(Request.Method.POST, EndPoints.GET_POSTS,
            Response.Listener {
                Log.d("response", it.toString())
                val jsonResponse = JSONObject(it)
                if (jsonResponse.getBoolean("success")) {
                    Toast.makeText(
                        this@AddPostActivity,
                        "post uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(this@AddPostActivity, "error", Toast.LENGTH_SHORT).show()
                }
                progress_dialog.dismiss()
            },
            Response.ErrorListener {
                it.printStackTrace()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val token_access = userSharePreferences.getString("token", "");
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token_access";
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["text"] = txtPostDescription.text.toString()
                params["photo"] = convertBitMapString(img_upload)
                return params
            }
        }
        SingleTonRequestQueueVolley.instance(this@AddPostActivity).add(strRequestSendPost)
    }

    private fun convertBitMapString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_CHANGE_IMAGE && resultCode == RESULT_OK) {
            val image_uri = data!!.data
            imgPostImage.setImageURI(image_uri)
            img_upload = MediaStore.Images.Media.getBitmap(contentResolver, image_uri)


        }
    }
}