package ir.danialchoopan.danialgram.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.R
import ir.danialchoopan.danialgram.UserInfoActivity
import kotlinx.android.synthetic.main.register_fragment_layout.*
import org.json.JSONObject
import java.util.HashMap

class RegisterFragment : Fragment(R.layout.register_fragment_layout) {
    lateinit var progressDialog: ProgressDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(context)
        tvLogin.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.frameAuthContainer, LoginFragment()).commit()
        }
        btnRegisterUser.setOnClickListener {
            if (validate()) {
                register()
            }
        }
        txtEmailRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (txtEmailRegister.text.toString().isNotEmpty()) {
                    txtLayoutEmailRegister.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        txtPasswordRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (txtPasswordRegister.text.toString().length > 7) {
                    txtLayoutPasswordRegister.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        txtConfirmPasswordRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (txtConfirmPasswordRegister.text.toString().length > 7
                    && txtPasswordRegister.text.toString() == txtConfirmPasswordRegister.text.toString()
                ) {
                    txtLayoutConfirmPasswordRegister.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun validate(): Boolean {
        if (txtEmailRegister.text.toString().isEmpty()) {
            txtLayoutEmailRegister.isErrorEnabled = true
            txtLayoutEmailRegister.error = "email is required"
            return false
        }
        if (txtPasswordRegister.text.toString().isEmpty()) {
            txtLayoutPasswordRegister.isErrorEnabled = true
            txtLayoutPasswordRegister.error = "password is required"
            return false
        }
        if (txtConfirmPasswordRegister.text.toString().isEmpty()) {
            txtLayoutConfirmPasswordRegister.isErrorEnabled = true
            txtLayoutConfirmPasswordRegister.error = "confirm is required"
            return false
        }

        if (txtPasswordRegister.text.toString().length < 8) {
            txtLayoutPasswordRegister.isErrorEnabled = true
            txtLayoutPasswordRegister.error = "required as list 8 charters"
            return false
        }

        if (txtConfirmPasswordRegister.text.toString().length < 8) {
            txtLayoutConfirmPasswordRegister.isErrorEnabled = true
            txtLayoutConfirmPasswordRegister.error = "required as list 8 charters"
            return false
        }

        if (txtConfirmPasswordRegister.text.toString() != txtPasswordRegister.text.toString()) {
            txtLayoutConfirmPasswordRegister.isErrorEnabled = true
            txtLayoutConfirmPasswordRegister.error = "passwords are not match"
            return false
        }
        return true
    }


    private fun register() {
        progressDialog.setTitle("waiting for result..")
        progressDialog.show()

//        Volley.newRequestQueue(activity).add(object : StringRequest(
//            Method.POST, EndPoints.REGISTER,
//            Response.Listener {
//                Log.d("ring_result", it)
//            },
//            Response.ErrorListener { }
//        ) {
//            override fun getParams(): MutableMap<String, String> {
//                val params = HashMap<String, String>()
//                params["email"] = "newuser@cfsafddwsa.com"
//                params["password"] = "dwqfdwqdqddwdwdwqqw"
//                return params
//            }
//        })

        val string_request_register =
            object :
                StringRequest(
                    Request.Method.POST,
                    EndPoints.REGISTER,
                    Response.Listener {
                        val json_result = JSONObject(it)
                        Log.d("reuslt_register", "${json_result}")
                        if (json_result.getBoolean("success")) {
                            val shareUser =
                                context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
                            val user = json_result.getJSONObject("user")
                            shareUser.edit().apply {
                                putInt("user_id", user.getInt("id"))
                                putString("token", json_result.getString("token"))
                                putString("name", user.getString("name"))
                                putString("last_name", user.getString("last_name"))
                                putString("photo", user.getString("photo"))
                                putBoolean("isUserLogin", true)
                                apply()
                            }
                            Toast.makeText(context, "register success", Toast.LENGTH_LONG).show()
                            Intent(activity, UserInfoActivity::class.java).also {
                                startActivity(it)
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "sorry somting wrong try again a little bit",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        progressDialog.dismiss()
                    }, Response.ErrorListener {
                        progressDialog.dismiss()
                    }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = txtEmailRegister.text.toString()
                    params["password"] = txtPasswordRegister.text.toString()
                    return params
                }
            }
        Volley.newRequestQueue(activity).add(string_request_register)
    }

}