package ir.danialchoopan.danialgram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import ir.danialchoopan.danialgram.adapters.SliderOnBoardViewPager2Adapter
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {
    var currentPositionSlider = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        view_pager_slider.adapter = SliderOnBoardViewPager2Adapter()
        view_pager_slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPositionSlider = position
                setCurrentPositionDots()
                if (position > 0) {
                    btn_left.text = "previous"
                } else {
                    btn_left.text = "skip"
                }
                if (position == 2) {
                    btn_right.text = "finish"
                } else {
                    btn_right.text = "next"
                }
                Log.d("currentPositionSlider", "${currentPositionSlider}")

            }

        })
        Log.d("currentPositionSlider", "${currentPositionSlider}")
        setCurrentPositionDots()
        btn_left.setOnClickListener {
            if (btn_left.text == "previous") {
                val item = view_pager_slider.currentItem--;
                view_pager_slider.setCurrentItem(item, true)
                view_pager_slider.currentItem = item
            } else if (btn_left.text == "skip") {
                Intent(this@OnBoardActivity, AuthActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

        btn_right.setOnClickListener {
            if (btn_left.text == "next") {
                val item = view_pager_slider.currentItem++;
                view_pager_slider.setCurrentItem(item, true)
                view_pager_slider.currentItem = item
            } else if (btn_left.text == "finish") {
                Intent(this@OnBoardActivity, AuthActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun setCurrentPositionDots() {
        dot_layout.removeAllViews()
        val ar_textViews = arrayListOf<TextView>()
        for (i in 1..3) {
            val textView = TextView(this@OnBoardActivity)
            textView.text = Html.fromHtml("&#8226")
            textView.textSize = 36f
            textView.setTextColor(resources.getColor(R.color.colorLightGrey))
            ar_textViews.add(textView)
            dot_layout.addView(textView)
        }
        ar_textViews[currentPositionSlider].setTextColor(resources.getColor(R.color.colorBlack))
        if (currentPositionSlider == 0) {
            ar_textViews[0].setTextColor(resources.getColor(R.color.colorBlack))
        }

    }
}