package com.example.hirupsehat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class GamifikasiItem(
    val title: String,
    val description: String,
    val points: String,
    val iconResId: Int,
    val isChecked: Boolean
)

class GamifikasiAdapter(
    private val items: List<GamifikasiItem>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<GamifikasiAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: GamifikasiItem)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_text)
        val iconView: ImageView = view.findViewById(R.id.item_icon)
        val pointsView: TextView = view.findViewById(R.id.item_points)
        val checkBox: CheckBox = view.findViewById(R.id.item_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gamifikasi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.title
        holder.iconView.setImageResource(item.iconResId)
        holder.pointsView.text = item.points
        holder.checkBox.isChecked = item.isChecked

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item)
        }
    }

    override fun getItemCount() = items.size
}