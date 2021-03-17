package ir.danialchoopan.danialgram.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import ir.danialchoopan.danialgram.adapters.UserPostsRecyclerAdapter
import ir.danialchoopan.danialgram.AuthActivity
import ir.danialchoopan.danialgram.EditUserActivity
import ir.danialchoopan.danialgram.models.PostDataModel
import ir.danialchoopan.danialgram.R
import ir.danialchoopan.danialgram.action.EndPoints
import ir.danialchoopan.danialgram.api.PostApi
import ir.danialchoopan.danialgram.api.UserApi
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shareUser = context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        recyclerViewUserPosts.layoutManager = GridLayoutManager(context, 2)
        val postApi = PostApi(context!!)
        val userApi = UserApi(context!!)
        postApi.getUserPosts(object : PostApi.Watcher {
            override fun getUserPosts(apiResult: ArrayList<PostDataModel>) {
                recyclerViewUserPosts.adapter = UserPostsRecyclerAdapter(apiResult)
                profileTvPostsCount.text = apiResult.size.toString()
            }
        })
        Picasso.get().load(EndPoints.URL + shareUser.getString("photo", ""))
            .into(profileImgUserProfile)
        profileTvUsername.text = shareUser.getString("name", "")
        btnOpenEditUser.setOnClickListener {
            Intent(context, EditUserActivity::class.java).also {
                startActivity(it)
            }
        }
        profileBtnLogout.setOnClickListener {
            userApi.logout()
            shareUser.edit().apply {
                clear()
                apply()
            }
            Intent(context, AuthActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }

    }
}