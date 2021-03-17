package ir.danialchoopan.danialgram.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.models.PostDataModel
import ir.danialchoopan.danialgram.models.UserModel
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import org.json.JSONObject

class PostApi(val context: Context) {
    val shareUser = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    fun getUserPosts(watcher: Watcher) {
        SingleTonRequestQueueVolley.instance(context)
            .add(object : StringRequest(Request.Method.GET, EndPoints.USER_POSTS,
                Response.Listener {
                    val ListPost = arrayListOf<PostDataModel>()
                    val json_response = JSONObject(it)
                    Log.d("response", it)
                    if (json_response.getBoolean("success")) {
                        val posts = json_response.getJSONArray("posts")
                        for (i in 0 until posts.length()) {
                            val postJsonObj = posts.getJSONObject(i)
                            ListPost.add(
                                PostDataModel(
                                    postJsonObj.getInt("id"),
                                    0,
                                    0,
                                    postJsonObj.getString("created_at"),
                                    postJsonObj.getString("text"),
                                    postJsonObj.getString("photo"),
                                    UserModel(
                                        0,
                                        "",
                                        ""
                                    ),
                                    false
                                )
                            )
                            watcher.getUserPosts(ListPost)
                        }
                    }
                },
                Response.ErrorListener { }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val token_access = shareUser.getString("token", "");
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token_access";
                    return headers
                }
            })
    }

    interface Watcher {
        fun getUserPosts(apiResult: ArrayList<PostDataModel>)
    }
}
