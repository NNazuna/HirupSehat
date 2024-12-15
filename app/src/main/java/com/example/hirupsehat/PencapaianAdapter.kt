package com.example.hirupsehat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class PencapaianItem(val iconResId: Int, val title: String)

class PencapaianAdapter(private val items: List<PencapaianItem>) :
    RecyclerView.Adapter<PencapaianAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconView: ImageView = view.findViewById(R.id.achievement_icon)
        val titleView: TextView = view.findViewById(R.id.achievement_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pencapaian, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.iconView.setImageResource(item.iconResId)
        holder.titleView.text = item.title
    }

    override fun getItemCount() = items.size
}