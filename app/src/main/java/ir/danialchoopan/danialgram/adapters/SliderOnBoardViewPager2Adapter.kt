package ir.danialchoopan.danialgram.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.danialchoopan.danialgram.models.SliderOnBoardDataModel
import ir.danialchoopan.danialgram.R
import kotlinx.android.synthetic.main.slide_view_pager2.view.*

class SliderOnBoardViewPager2Adapter :
    RecyclerView.Adapter<SliderOnBoardViewPager2Adapter.ViewHolder>
        () {
    val ar_data = listOf<SliderOnBoardDataModel>(
        SliderOnBoardDataModel(
            R.drawable.p1,
            "Create",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard"
        ),
        SliderOnBoardDataModel(
            R.drawable.p2,
            "Learn",
            "2Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard"
        ),
        SliderOnBoardDataModel(
            R.drawable.p3,
            "Enjoy",
            "3Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard"
        ),
    )

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.slide_view_pager2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = ar_data[position]
        holder.view.img_view_pager.setImageResource(dataModel.img)
        holder.view.tv_view_pager_title.text = dataModel.title
        holder.view.tv_view_pager_description.text = dataModel.description
    }

    override fun getItemCount(): Int = ar_data.size
}