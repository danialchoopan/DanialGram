package ir.danialchoopan.danialgram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import ir.danialchoopan.danialgram.fragments.HomeFragment
import ir.danialchoopan.danialgram.fragments.UserProfileFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    var currentTime = 0L
    val intervalTime = 2000L
    val CODE_ADD_POST_IMAGE = 113
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val shareUser = getSharedPreferences("user", Context.MODE_PRIVATE)
        supportFragmentManager.beginTransaction().replace(R.id.frm_home_frame, HomeFragment())
            .commit()

        floatingActionButton.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                startActivityForResult(it, CODE_ADD_POST_IMAGE)

            }
        }
        Log.d("user_id", "${shareUser.getInt("user_id", 0)}")

        bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_home ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frm_home_frame, HomeFragment())
                        .commit()
                }
                R.id.item_profile ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frm_home_frame, UserProfileFragment())
                        .commit()
                }
            }
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_ADD_POST_IMAGE && resultCode == RESULT_OK) {
            Toast.makeText(this@HomeActivity, "dasda", Toast.LENGTH_SHORT).show()
            val img_uri = data!!.data
            Intent(this@HomeActivity, AddPostActivity::class.java).also {
                it.data = img_uri
                startActivity(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_search, menu)
        val search_view = menu!!.getItem(R.id.app_bar_search).actionView as SearchView
//        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//
//            }
//
//        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if ((currentTime + intervalTime) > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Toast.makeText(
                this@HomeActivity,
                "برای خروح لطفا دو بار کلیک کنید.",
                Toast.LENGTH_SHORT
            ).show()
            currentTime = System.currentTimeMillis()

        }
    }
}