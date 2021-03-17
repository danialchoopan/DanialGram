package ir.danialchoopan.danialgram.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ir.danialchoopan.danialgram.CommentsActivity
import ir.danialchoopan.danialgram.EditActivity
import ir.danialchoopan.danialgram.models.PostDataModel
import ir.danialchoopan.danialgram.R
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.action.ImgDownloader
import ir.danialchoopan.danialgram.action.SingleTonRequestQueueVolley
import kotlinx.android.synthetic.main.posts_row.view.*
import org.json.JSONObject
import org.koin.core.component.KoinComponent

class PostsAdapterRecyclerView(val context: Context, val ar_data: ArrayList<PostDataModel>) :
    RecyclerView.Adapter<PostsAdapterRecyclerView.ViewHolder>(), KoinComponent {
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    val shareUser = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    val list_all = ArrayList(ar_data)
    val imgDownloader = ImgDownloader()
    var current_time = 0L

    val fillter = object : android.widget.Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = ArrayList<PostDataModel>()
            if (p0.toString().isEmpty()) {
                filteredList.addAll(list_all)
            } else {
                for (post in list_all) {
                    if (post.description.toLowerCase().contains(p0.toString().toLowerCase()) &&
                        post.user.userName.toLowerCase().contains(p0.toString().toLowerCase())
                    ) {
                        filteredList.add(post)
                    }
                }

            }

            val filtered_result = FilterResults()
            filtered_result.values = filteredList
            return filtered_result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            ar_data.clear()
            ar_data.addAll(p1!!.values as Collection<PostDataModel>)
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.posts_row, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = ar_data[position]
        Log.d("data_result", EndPoints.URL + post.user.photo)
        imgDownloader.init(EndPoints.URL + post.user.photo, holder.view.postImgUserProfile)
        imgDownloader.init(EndPoints.URL + post.photo, holder.view.postImgPostShow)
        holder.view.postTvUsername.text = post.user.userName
        holder.view.postTvDescription.text = post.description
        holder.view.postTvPostDate.text = post.date
        holder.view.postTvLikes.text = "Likes ${post.likes}"
        holder.view.tvCommentsCount.text = "comments ${post.comments}"
        if (post.selfLike) {
            holder.view.imgBtnLike.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            holder.view.imgBtnLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }


        holder.view.postImgPostShow.setOnClickListener {
            if ((current_time + 1000) > System.currentTimeMillis()) {
                Toast.makeText(context, "double click worded", Toast.LENGTH_SHORT).show()

                if (!post.selfLike) {
                    holder.view.imgBtnLike.setImageResource(R.drawable.ic_baseline_favorite_24)

                    post.likes = +1
                    holder.view.postTvLikes.text = "Likes ${post.likes}"
                    SingleTonRequestQueueVolley.instance(context)
                        .add(object :
                            StringRequest(Request.Method.POST, EndPoints.LIKE_POST,
                                Response.Listener {
                                    Log.d("response", it)
                                },
                                Response.ErrorListener {
                                    it.printStackTrace()
                                }) {
                            override fun getParams(): MutableMap<String, String> {
                                val params = HashMap<String, String>()
                                params["post_id"] = post.id.toString()
                                return params
                            }

                            override fun getHeaders(): MutableMap<String, String> {
                                val token_access = shareUser.getString("token", "");
                                val headers = HashMap<String, String>()
                                headers["Authorization"] = "Bearer $token_access";
                                return headers
                            }
                        })


                } else {
                    holder.view.imgBtnLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    if (post.likes != 0) {
                        post.likes -= 1
                    } else {
                        post.likes = 0
                    }
                    holder.view.postTvLikes.text = "Likes ${post.likes}"
                    SingleTonRequestQueueVolley.instance(context)
                        .add(object :
                            StringRequest(Request.Method.POST, EndPoints.LIKE_POST,
                                Response.Listener {
                                    Log.d("response", it)
                                },
                                Response.ErrorListener {
                                    it.printStackTrace()

                                }) {
                            override fun getParams(): MutableMap<String, String> {
                                val params = HashMap<String, String>()
                                params["post_id"] = post.id.toString()
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


            } else {
                current_time = System.currentTimeMillis()
            }
        }

        holder.view.imgBtnLike.setOnClickListener {
            post.selfLike = !post.selfLike;
            if (post.selfLike) {
                holder.view.imgBtnLike.setImageResource(R.drawable.ic_baseline_favorite_24)

                post.likes = +1
                holder.view.postTvLikes.text = "Likes ${post.likes}"
                SingleTonRequestQueueVolley.instance(context)
                    .add(object :
                        StringRequest(Request.Method.POST, EndPoints.LIKE_POST,
                            Response.Listener {
                                Log.d("response", it)
                            },
                            Response.ErrorListener {
                                it.printStackTrace()
                            }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params["post_id"] = post.id.toString()
                            return params
                        }

                        override fun getHeaders(): MutableMap<String, String> {
                            val token_access = shareUser.getString("token", "");
                            val headers = HashMap<String, String>()
                            headers["Authorization"] = "Bearer $token_access";
                            return headers
                        }
                    })


            } else {
                holder.view.imgBtnLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                if (post.likes != 0) {
                    post.likes -= 1
                } else {
                    post.likes = 0
                }
                holder.view.postTvLikes.text = "Likes ${post.likes}"
                SingleTonRequestQueueVolley.instance(context)
                    .add(object :
                        StringRequest(Request.Method.POST, EndPoints.LIKE_POST,
                            Response.Listener {
                                Log.d("response", it)
                            },
                            Response.ErrorListener {
                                it.printStackTrace()

                            }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params["post_id"] = post.id.toString()
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

        }

        if (post.user.id == shareUser.getInt("user_id", 0)) {
            holder.view.imgBtnMorePost.visibility = View.VISIBLE
        } else {
            holder.view.imgBtnMorePost.visibility = View.GONE
        }
        holder.view.imgBtnComment.setOnClickListener {
            Intent(context, CommentsActivity::class.java).also { intent ->
                intent.putExtra("post_id", post.id)
                context.startActivity(intent)
            }
        }
        holder.view.imgBtnMorePost.setOnClickListener {
            val popupmenu = PopupMenu(context, holder.view.imgBtnMorePost).apply {
                inflate(R.menu.menu_more_post)
            }
            popupmenu.setOnMenuItemClickListener { item_menu ->
                when (item_menu.itemId) {
                    R.id.item_edit -> {
                        Intent(context, EditActivity::class.java).also {
                            it.putExtra("post_id", post.id)
                            it.putExtra("post_text", post.description)
                            context.startActivity(it)
                        }
                    }
                    R.id.item_delete -> {
                        AlertDialog.Builder(context).setTitle("delete")
                            .setMessage("delete this post ? ").setPositiveButton("delete") { _, _ ->
                                SingleTonRequestQueueVolley.instance(context)
                                    .add(object : StringRequest(Request.Method.DELETE,
                                        EndPoints.DELETE_POST + post.id,
                                        Response.Listener {
                                            notifyDataSetChanged()
                                            Log.d("response", it)
                                            val jsonResult = JSONObject(it)
                                            if (jsonResult.getBoolean("success")) {
                                                Toast.makeText(
                                                    context,
                                                    "post deleted successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "post not deleted ",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        Response.ErrorListener { }) {
                                        override fun getHeaders(): MutableMap<String, String> {
                                            val token_access =
                                                shareUser.getString("token", "");
                                            val headers = HashMap<String, String>()
                                            headers["Authorization"] = "Bearer $token_access";
                                            return headers
                                        }
                                    })
                            }.setNegativeButton("cancel") { _, _ ->

                            }
                            .show()
                    }
                }
                false
            }
            popupmenu.show()
        }


    }

    override fun getItemCount(): Int = ar_data.size
}