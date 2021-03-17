package ir.danialchoopan.danialgram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.danialchoopan.danialgram.models.CommentDataModel
import ir.danialchoopan.danialgram.R
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.api.PostCommentsAPI
import kotlinx.android.synthetic.main.comments_row.view.*

class CommentsAdapterRecyclerView(val context: Context, val ar_data: ArrayList<CommentDataModel>) :
    RecyclerView.Adapter<CommentsAdapterRecyclerView.ViewHolder>() {
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comments_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment_model = ar_data[position]
        Picasso.get().load(EndPoints.URL + comment_model.user.photo)
            .into(holder.view.commentImgUserProfile)

        holder.view.commentTvUsername.text = comment_model.user.userName
        holder.view.commentTvPostDate.text = comment_model.date
        holder.view.commentTvComment.text = comment_model.comment

        holder.view.commentBtnMoreComment.setOnClickListener {
            val popupmenu = PopupMenu(context, holder.view.commentBtnMoreComment)
            popupmenu.inflate(R.menu.menu_comment_item)
            popupmenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_delete_comment -> {
                        val postCommentsAPI = PostCommentsAPI(context)
                        postCommentsAPI.deleteComment(comment_model.id)
                        notifyDataSetChanged()
                    }
                }
                false
            }
            popupmenu.show()
        }
    }

    override fun getItemCount(): Int = ar_data.size

}