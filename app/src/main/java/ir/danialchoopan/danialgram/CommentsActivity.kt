package ir.danialchoopan.danialgram

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ir.danialchoopan.danialgram.adapters.CommentsAdapterRecyclerView
import ir.danialchoopan.danialgram.models.CommentDataModel
import ir.danialchoopan.danialgram.api.PostCommentsAPI
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity(), PostCommentsAPI.Watcher {
    lateinit var shareUser: SharedPreferences
    var postId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        shareUser = getSharedPreferences("user", Context.MODE_PRIVATE)
        postId = intent.getIntExtra("post_id", 0)
        Log.d("post_id_comments", postId.toString())
        val postCommentsAPI = PostCommentsAPI(this@CommentsActivity)
        postCommentsAPI.getPostComments(this@CommentsActivity, postId)
        btnAddComment.setOnClickListener {
            postCommentsAPI.AddComment(txtAddComment.text.toString(), postId)
            txtAddComment.setText("")
            postCommentsAPI.getPostComments(this@CommentsActivity, postId)
        }
        imgBtnBackHomeComments.setOnClickListener {
            finish()
        }
//        recyclerViewComments.

    }

    override fun GetComments(apiResult: ArrayList<CommentDataModel>) {
        val adapterComment = CommentsAdapterRecyclerView(this@CommentsActivity, apiResult)
        recyclerViewComments.layoutManager = LinearLayoutManager(this@CommentsActivity)
        recyclerViewComments.adapter = adapterComment

    }

}