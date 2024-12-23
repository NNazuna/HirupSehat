package com.example.hirupsehat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirupsehat.databinding.ActivityDailyMissionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class DailyMissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyMissionBinding
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var currentDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyMissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userId = currentUser.uid
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().reference
        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        checkAndResetMissions()
    }

    private fun checkAndResetMissions() {
        val lastResetDateRef = database.child("users").child(userId).child("lastResetDate")
        val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        lastResetDateRef.get().addOnSuccessListener { snapshot ->
            val lastResetDate = snapshot.getValue(String::class.java)

            if (lastResetDate != todayDate) {
                resetMissions(todayDate)
            } else {
                loadMissions()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to check reset status", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetMissions(todayDate: String) {
        val missionDefaults = mapOf(
            "Login" to false,
            "Minum Air Putih" to false,
            "Olahraga Ringan" to false,
            "Kurangi Rokok" to false
        )

        val missionPath = database.child("users").child(userId).child("dailyMission").child(todayDate)
        missionPath.setValue(missionDefaults).addOnSuccessListener {
            database.child("users").child(userId).child("lastResetDate").setValue(todayDate)
            loadMissions()
            Toast.makeText(this, "Missions have been reset for today!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to reset missions", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadMissions() {
        database.child("users").child(userId).child("dailyMission").child(currentDate)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val missions = mutableListOf<Mission>()

                    if (snapshot.exists()) {
                        for (mission in snapshot.children) {
                            val title = mission.key ?: "Unknown"
                            val isCompleted = mission.getValue(Boolean::class.java) ?: false
                            val points = when (title) {
                                "Login" -> 10
                                "Minum Air Putih" -> 15
                                "Olahraga Ringan" -> 20
                                "Kurangi Rokok" -> 20
                                else -> 0
                            }
                            missions.add(Mission(title, "$points Points", points, isCompleted))
                        }
                    } else {
                        missions.addAll(
                            listOf(
                                Mission("Login", "Masuk ke aplikasi Hirup Sehat", 10, false),
                                Mission("Minum Air Putih", "Minum air putih 8 gelas", 15, false),
                                Mission("Olahraga Ringan", "Jalan kaki 1000 langkah", 20, false),
                                Mission("Kurangi Rokok", "Kurangi konsumsi rokok 1 batang", 20, false)
                            )
                        )
                    }

                    setupRecyclerView(missions)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DailyMissionActivity, "Failed to load missions.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupRecyclerView(missions: List<Mission>) {
        val adapter = MissionAdapter(missions, database, userId, currentDate)
        binding.recyclerViewMissions.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMissions.adapter = adapter
    }
}
