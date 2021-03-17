package ir.danialchoopan.danialgram.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.adapters.PostsAdapterRecyclerView
import ir.danialchoopan.danialgram.AuthActivity
import ir.danialchoopan.danialgram.models.PostDataModel
import ir.danialchoopan.danialgram.models.UserModel
import ir.danialchoopan.danialgram.R
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject


class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var shareUser: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareUser = context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        swipe_home.setOnRefreshListener {
            getPosts()
        }
        getPosts()

    }

    private fun getPosts() {
        swipe_home.isRefreshing = true
        SingleTonRequestQueueVolley.instance(context!!).add(
            object :
                StringRequest(Method.GET, EndPoints.GET_POSTS, Response.Listener {
                    val ListPost = arrayListOf<PostDataModel>()
                    val json_response = JSONObject(it)
                    Log.d("response", it)
                    if (json_response.getBoolean("success")) {
                        val posts = json_response.getJSONArray("posts")
                        for (i in 0 until posts.length()) {
                            val postJsonObj = posts.getJSONObject(i)
                            val userJson = postJsonObj.getJSONObject("user")
                            ListPost.add(
                                PostDataModel(
                                    postJsonObj.getInt("id"),
                                    postJsonObj.getInt("likeCount"),
                                    postJsonObj.getInt("commentCount"),
                                    postJsonObj.getString("created_at"),
                                    postJsonObj.getString("text"),
                                    postJsonObj.getString("photo"),
                                    UserModel(
                                        userJson.getInt("id"),
                                        userJson.getString("name"),
                                        userJson.getString("photo")
                                    ),
                                    postJsonObj.getBoolean("selfLike")
                                )
                            )
                        }
                        recycler_posts_home.layoutManager = LinearLayoutManager(context)
                        recycler_posts_home.adapter = PostsAdapterRecyclerView(context!!, ListPost)

                    } else {
                        Toast.makeText(context, "error json", Toast.LENGTH_SHORT).show()
                        Intent(activity, AuthActivity::class.java).also {
                            startActivity(it)
                        }
                        activity!!.finish()
                    }

                    swipe_home.isRefreshing = false
                }, Response.ErrorListener {
                    swipe_home.isRefreshing = false
                }) {
                val token_access = shareUser.getString("token", "")
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token_access";
                    return headers
                }
            })
    }

}