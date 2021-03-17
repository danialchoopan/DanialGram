package ir.danialchoopan.danialgram.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.danialchoopan.danialgram.models.PostDataModel
import ir.danialchoopan.danialgram.R
import ir.danialchoopan.danialgram.action.EndPoints
import kotlinx.android.synthetic.main.user_posts_row.view.*

class UserPostsRecyclerAdapter(val arUserPosts: ArrayList<PostDataModel>) :
    RecyclerView.Adapter<UserPostsRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_posts_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(EndPoints.URL + arUserPosts[position].photo)
            .into(holder.view.imgUserPosts)

    }

    override fun getItemCount(): Int = arUserPosts.size
}