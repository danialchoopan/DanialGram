package ir.danialchoopan.danialgram

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import ir.danialchoopan.danialgram.action.EndPoints
import kotlinx.android.synthetic.main.activity_user_info.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap

class UserInfoActivity : AppCompatActivity() {
    val CODE_IMAGE_PICK = 101;
    lateinit var img_pick: Bitmap
    lateinit var userSharePreferences: SharedPreferences
    lateinit var progressDialog: ProgressDialog
    private var imageData: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        progressDialog = ProgressDialog(this@UserInfoActivity)
        userSharePreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
//        Log.d("user_info", userSharePreferences.getBoolean("isUserInfoCompiled", false).toString())
//        if (userSharePreferences.getBoolean("isUserInfoCompiled", false)) {
//            Intent(this@UserInfoActivity, HomeActivity::class.java).also {
//                startActivity(it)
//            }
//            finish()
//        }

        check_validation()
        btnSaveUserInfo.setOnClickListener {
            if (validate()) {
                complete_user_info()
            }
        }
        tvSelectImage.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                startActivityForResult(it, CODE_IMAGE_PICK)
            }
        }
    }

    private fun complete_user_info() {
        progressDialog.setCancelable(false)
        progressDialog.setTitle("please waite .. ")
        progressDialog.show()
        val userInfoStringRequest =
            object : StringRequest(Request.Method.POST, EndPoints.SAVE_USER_INFO,
                Response.Listener {
                    Log.d("result_data", it)
                    progressDialog.dismiss()
                    val json_result = JSONObject(it)
                    if (json_result.getBoolean("success")) {
                        val json_user = json_result.getJSONObject("user")
                        userSharePreferences.edit().apply {
                            putString("photo", json_user.getString("photo"))
                            putBoolean("isUserInfoCompiled", true)
                            apply()
                        }
                        Toast.makeText(
                            this@UserInfoActivity,
                            "done",
                            Toast.LENGTH_LONG
                        ).show()
                        Intent(this@UserInfoActivity, HomeActivity::class.java).also { intent ->
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(
                            this@UserInfoActivity,
                            "error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener {
                    progressDialog.dismiss()
                    it.message?.let { it1 -> Log.d("body erorr", it1) }
                    it.printStackTrace()
                    Toast.makeText(
                        this@UserInfoActivity,
                        "error response",
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val token_access = userSharePreferences.getString("token", "");
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token_access";
                    return headers
                }

                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = txtName.text.toString()
                    params["last_name"] = txtLastName.text.toString()
                    params["photo"] = convertBitMapString(img_pick)
                    return params
                }
            }
        Volley.newRequestQueue(this@UserInfoActivity).add(userInfoStringRequest)

    }

    private fun convertBitMapString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            val img_uri = data!!.data
            imgUserProfile.setImageURI(img_uri)
            img_pick = MediaStore.Images.Media.getBitmap(contentResolver, img_uri)
        }
    }

    private fun validate(): Boolean {
        if (txtName.text.toString().isEmpty()) {
            txtLayoutName.isErrorEnabled = true
            txtLayoutName.error = "name is required"
            return false
        }
        if (txtLastName.text.toString().isEmpty()) {
            txtLayoutLastName.isErrorEnabled = true
            txtLayoutLastName.error = "last name is required"
            return false
        }
        return true
    }

    private fun check_validation() {
        txtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (txtName.text.toString().isNotEmpty()) {
                    txtLayoutName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        txtLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (txtLastName.text.toString().isNotEmpty()) {
                    txtLayoutLastName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}