package com.example.hirupsehat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class MissionAdapter(
    private val missions: List<Mission>,
    private val database: DatabaseReference,
    private val userId: String,
    private val date: String
) : RecyclerView.Adapter<MissionAdapter.MissionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mission, parent, false)
        return MissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val mission = missions[position]
        holder.title.text = mission.title
        holder.description.text = mission.description
        holder.points.text = "${mission.points} Points"
        holder.checkBox.isChecked = mission.isCompleted

        holder.checkBox.setOnClickListener {
            if (mission.isCompleted) {
                Toast.makeText(
                    holder.itemView.context,
                    "Tunggu sampai jam 04.00 WIB untuk melakukan tugas ini.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mission.isCompleted = true
                holder.checkBox.isChecked = true
                saveMissionToDatabase(mission)
                Toast.makeText(
                    holder.itemView.context,
                    "Mission completed! +${mission.points} points added.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = missions.size

    private fun saveMissionToDatabase(mission: Mission) {
        database.child("users").child(userId).child("dailyMission").child(date).child(mission.title)
            .setValue(true)
        updatePoints(mission.points)
    }

    private fun updatePoints(points: Int) {
        val userPointsRef = database.child("users").child(userId).child("points")
        userPointsRef.runTransaction(object : com.google.firebase.database.Transaction.Handler {
            override fun doTransaction(currentData: com.google.firebase.database.MutableData): com.google.firebase.database.Transaction.Result {
                val currentPoints = currentData.getValue(Int::class.java) ?: 0
                currentData.value = currentPoints + points
                return com.google.firebase.database.Transaction.success(currentData)
            }

            override fun onComplete(
                error: com.google.firebase.database.DatabaseError?,
                committed: Boolean,
                currentData: com.google.firebase.database.DataSnapshot?
            ) {
                // Points updated successfully
            }
        })
    }

    inner class MissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewMissionTitle)
        val description: TextView = itemView.findViewById(R.id.textViewMissionDescription)
        val points: TextView = itemView.findViewById(R.id.textViewMissionPoints)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxMission)
    }
}
