package com.example.hirupsehat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hirupsehat.databinding.ActivityMiniGamesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MiniGamesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMiniGamesBinding
    private val sequence = mutableListOf<String>()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var database: DatabaseReference
    private var currentUID: String? = null
    private var wrongAttempts = 0 // Counter untuk jawaban salah

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiniGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan referensi Firebase Database
        database = FirebaseDatabase.getInstance().reference
        currentUID = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUID == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        generateSequence()
        displaySequence()

        binding.submitButton.setOnClickListener {
            val userInput = binding.userInput.text.toString().trim().replace(" ", "")
            val correctAnswer = sequence.joinToString(separator = "")

            Log.d("MiniGamesActivity", "User Input: $userInput")
            Log.d("MiniGamesActivity", "Correct Answer: $correctAnswer")

            if (userInput == correctAnswer) {
                binding.instructionText.text = getString(R.string.correct_message)
                binding.userInput.text.clear()
                wrongAttempts = 0 // Reset jumlah salah
                updatePoints(2) // Tambahkan 2 poin
                generateSequence()
                displaySequence()
            } else {
                wrongAttempts++
                binding.instructionText.text = getString(R.string.incorrect_message)
                binding.userInput.text.clear()

                if (wrongAttempts >= 3) {
                    Toast.makeText(this, "Anda sudah salah 3x, petunjuk akan diganti", Toast.LENGTH_LONG).show()
                    generateSequence() // Ganti petunjuk
                    displaySequence() // Tampilkan petunjuk baru
                    wrongAttempts = 0 // Reset jumlah salah
                }
            }
        }
    }

    private fun updatePoints(pointsToAdd: Int) {
        currentUID?.let { uid ->
            val pointsRef = database.child("users").child(uid).child("points")
            pointsRef.get().addOnSuccessListener { snapshot ->
                val currentPoints = snapshot.getValue(Int::class.java) ?: 0
                val updatedPoints = currentPoints + pointsToAdd

                pointsRef.setValue(updatedPoints).addOnSuccessListener {
                    Log.d("MiniGamesActivity", "Points updated successfully to $updatedPoints")
                }.addOnFailureListener { error ->
                    Log.e("MiniGamesActivity", "Failed to update points: ${error.message}")
                }
            }.addOnFailureListener { error ->
                Log.e("MiniGamesActivity", "Failed to fetch points: ${error.message}")
            }
        }
    }

    private fun generateSequence() {
        sequence.clear()
        repeat(5) { // Generate 5 angka random
            val randomNumber = (0..9).random().toString()
            sequence.add(randomNumber)
        }
        Log.d("MiniGamesActivity", "Generated sequence: $sequence")
    }

    private fun displaySequence() {
        handler.postDelayed({
            binding.sequenceText.text = ""
        }, 1000) // Angka akan hilang setelah 1 detik

        binding.sequenceText.text = sequence.joinToString(" ")
    }
}
