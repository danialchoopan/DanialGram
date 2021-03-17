package ir.danialchoopan.danialgram.action

import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImgDownloader {
    fun init(uri: String, imgView: ImageView) {
        Picasso.get().load(uri).into(imgView)
    }
}