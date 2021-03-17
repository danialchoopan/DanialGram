package ir.danialchoopan.danialgram.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.R
import ir.danialchoopan.danialgram.UserInfoActivity
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import kotlinx.android.synthetic.main.login_fragment_layout.*
import org.json.JSONObject
import java.util.HashMap

class LoginFragment : Fragment(R.layout.login_fragment_layout) {
    lateinit var progressDialog: ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(context)

        tvRegister.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameAuthContainer, RegisterFragment()).commit()
        }
        btnLoginUser.setOnClickListener {
            if (validate()) {
                login()
            }
        }

        txtEmailLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (txtEmailLogin.text.toString().isNotEmpty()) {
                    txtLayoutEmailLogin.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        txtPasswordLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (txtPasswordLogin.text.toString().length > 7) {
                    txtLayoutPasswordLogin.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun validate(): Boolean {
        if (txtEmailLogin.text.toString().isEmpty()) {
            txtLayoutEmailLogin.isErrorEnabled = true
            txtLayoutEmailLogin.error = "email is required"
            return false
        }
        if (txtPasswordLogin.text.toString().isEmpty()) {
            txtLayoutPasswordLogin.isErrorEnabled = true
            txtLayoutPasswordLogin.error = "password is required"
            return false
        }


        if (txtPasswordLogin.text.toString().length < 8) {
            txtLayoutPasswordLogin.isErrorEnabled = true
            txtLayoutPasswordLogin.error = "required as list 8 charters"
            return false
        }

        return true
    }


    private fun login() {
        progressDialog.setMessage("Loading... ")
        progressDialog.show()
        SingleTonRequestQueueVolley.instance(requireContext()).add(object :
            StringRequest(
                Method.POST,
                EndPoints.LOGIN,
                Response.Listener {
                    val json_result = JSONObject(it)
                    if (json_result.getBoolean("success")) {
                        val userShare =
                            requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
                        val user = json_result.getJSONObject("user")
                        userShare.edit().apply {
                            putInt("user_id", user.getInt("id"))
                            putString("token", json_result.getString("token"))
                            putString("name", user.getString("name"))
                            putString("last_name", user.getString("last_name"))
                            putString("photo", user.getString("photo"))
                            putBoolean("isUserLogin", true)
                            apply()
                        }
                        if (userShare.getString("name", "").toString().isNotEmpty()
                            && userShare.getString("last_name", "").toString().isNotEmpty()
                        ) {
                            userShare.edit().apply {
                                putBoolean("isUserInfoCompiled", true)
                                apply()
                            }
                        }
                        Toast.makeText(context, "login success", Toast.LENGTH_LONG).show()
                        Intent(activity, UserInfoActivity::class.java).also { intent ->
                            startActivity(intent)
                            activity?.finish()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "please check username or password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    progressDialog.dismiss()
                }, Response.ErrorListener {
                    progressDialog.dismiss()
                }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = txtEmailLogin.text.toString()
                params["password"] = txtPasswordLogin.text.toString()
                return params
            }
        })
    }


}