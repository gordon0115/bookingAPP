package com.fyp.pj.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.pj.R

class NewsAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView
        val tvDes: TextView

        init {
            tvDate = view.findViewById(R.id.tvDate)
            tvDes = view.findViewById(R.id.tvDes)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_news, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvDes.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size

}
