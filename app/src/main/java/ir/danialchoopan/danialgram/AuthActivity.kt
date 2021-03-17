package ir.danialchoopan.danialgram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.danialchoopan.danialgram.fragments.RegisterFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportFragmentManager.beginTransaction().replace(R.id.frameAuthContainer, RegisterFragment())
            .commit()
    }
}