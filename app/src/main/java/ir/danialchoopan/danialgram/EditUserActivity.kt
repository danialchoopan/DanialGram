package ir.danialchoopan.danialgram

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
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
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.ImgDownloader
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_user_info.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class EditUserActivity : AppCompatActivity() {
    val CODE_IMAGE_PICK = 101;
    lateinit var img_pick: Bitmap
    lateinit var userSharePreferences: SharedPreferences
    lateinit var progressDialog: ProgressDialog
    private var imageData: ByteArray? = null
    private val imgDownloader = ImgDownloader()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        progressDialog = ProgressDialog(this@EditUserActivity)
        userSharePreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        imgDownloader.init(
            EndPoints.URL + userSharePreferences.getString("photo", "")!!,
            updateImgUserProfile
        )
        updateTxtName.setText(userSharePreferences.getString("name",""))
        updateTxtLastName.setText(userSharePreferences.getString("last_name",""))
        check_validation()
        updateBtnSaveUserInfo.setOnClickListener {
            if (validate()) {
                complete_user_info()
            }
        }
        updateTvSelectImage.setOnClickListener {
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
        SingleTonRequestQueueVolley.instance(this@EditUserActivity).add(
            object : StringRequest(
                Request.Method.POST, EndPoints.SAVE_USER_INFO,
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
                            this@EditUserActivity,
                            "done",
                            Toast.LENGTH_LONG
                        ).show()
                        Intent(this@EditUserActivity, HomeActivity::class.java).also { intent ->
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(
                            this@EditUserActivity,
                            "error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener {
                    progressDialog.dismiss()
                    it.printStackTrace()
                    Toast.makeText(
                        this@EditUserActivity,
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
                    params["name"] = updateTxtName.text.toString()
                    params["last_name"] = updateTxtLastName.text.toString()
                    params["photo"] = convertBitMapString(img_pick)
                    return params
                }
            })

    }

    private fun createImageData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
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
            updateImgUserProfile.setImageURI(img_uri)
            img_pick = MediaStore.Images.Media.getBitmap(contentResolver, img_uri)
        }
    }

    private fun validate(): Boolean {
        if (updateTxtName.text.toString().isEmpty()) {
            updateTxtLayoutName.isErrorEnabled = true
            updateTxtLayoutName.error = "name is required"
            return false
        }
        if (updateTxtLastName.text.toString().isEmpty()) {
            updateTxtLayoutLastName.isErrorEnabled = true
            updateTxtLayoutLastName.error = "last name is required"
            return false
        }
        return true
    }

    private fun check_validation() {
        updateTxtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (updateTxtName.text.toString().isNotEmpty()) {
                    updateTxtLayoutName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        updateTxtLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (updateTxtLastName.text.toString().isNotEmpty()) {
                    updateTxtLayoutLastName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}