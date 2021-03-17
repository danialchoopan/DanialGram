package ir.danialchoopan.danialgram.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.models.CommentDataModel
import ir.danialchoopan.danialgram.models.UserModel
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import org.json.JSONObject

class PostCommentsAPI(val context: Context) {
    val shareUser = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun getPostComments(
        watcher: PostCommentsAPI.Watcher,
        postId: Int
    ) {
        SingleTonRequestQueueVolley.instance(context)
            .add(object :
                StringRequest(Method.GET, EndPoints.COMMENTS_POST + postId,
                    Response.Listener {
                        Log.d("response", it)
                        val jsonResult = JSONObject(it)
                        if (jsonResult.getBoolean("success")) {
                            val postCommentsArrayList = ArrayList<CommentDataModel>()
                            val commentsJson = jsonResult.getJSONArray("comments")
                            for (i in 0 until commentsJson.length()) {
                                val commentJsonItem = commentsJson.getJSONObject(i)
                                postCommentsArrayList.add(
                                    CommentDataModel(
                                        commentJsonItem.getInt("id"),
                                        commentJsonItem.getString("created_at"),
                                        commentJsonItem.getString("comment"),
                                        UserModel(
                                            commentJsonItem.getJSONObject("user").getInt("id"),
                                            commentJsonItem.getJSONObject("user").getString("name"),
                                            commentJsonItem.getJSONObject("user").getString("photo")
                                        )
                                    )
                                )
                            }
                            watcher.GetComments(postCommentsArrayList)
                        } else {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
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

    fun AddComment(comment: String, postId: Int) {
        SingleTonRequestQueueVolley.instance(context)
            .add(object :
                StringRequest(Method.POST, EndPoints.COMMENTS_POST,
                    Response.Listener {
                        Log.d("response", it)
                        val jsonResult = JSONObject(it)
                        if (jsonResult.getBoolean("success")) {
                            Toast.makeText(context, "commentAdded", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        it.printStackTrace()
                    }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["post_id"] = postId.toString()
                    params["comment"] = comment
                    return params
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val token_access = shareUser.getString("token", "");
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token_access";
                    return headers
                }
            })
    }

    fun deleteComment(commentId: Int) {
        SingleTonRequestQueueVolley.instance(context).add(
            object : StringRequest(Request.Method.DELETE, EndPoints.DELETE_COMMENT+commentId,
                Response.Listener {
                    val jsonResult = JSONObject(it)
                    if (jsonResult.getBoolean("success")) {
                        Toast.makeText(context, "comment deleted successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "error delete comment", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val token_access = shareUser.getString("token", "");
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token_access";
                    return headers
                }
            }
        )
    }

    interface Watcher {
        fun GetComments(apiResult: ArrayList<CommentDataModel>)
    }
}