package com.example.hirupsehat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var handler: Handler
    private var currentStreak: Long = 0
    private var longestStreak: Long = 0
    private var isRunning = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)
        currentStreak = sharedPreferences.getLong("current_streak", 0)
        longestStreak = sharedPreferences.getLong("longest_streak", 0)

        val streakDaysTextView: TextView = view.findViewById(R.id.streak_days)
        val currentStreakTextView: TextView = view.findViewById(R.id.current_streak)
        val longestStreakTextView: TextView = view.findViewById(R.id.longest_streak)
        val buttonSmoke: Button = view.findViewById(R.id.button_smoke)

        handler = Handler()
        startStreakTimer(streakDaysTextView, currentStreakTextView)

        buttonSmoke.setOnClickListener {
            resetStreak(streakDaysTextView, currentStreakTextView, longestStreakTextView)
        }

        longestStreakTextView.text = "Streak Terlama: ${formatTime(longestStreak)}"

        return view
    }

    private fun startStreakTimer(streakDaysTextView: TextView, currentStreakTextView: TextView) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isRunning) {
                    currentStreak++
                    streakDaysTextView.text = (currentStreak / 86400).toString() // Convert seconds to days
                    currentStreakTextView.text = "Streak Sekarang: ${formatTime(currentStreak)}"
                    sharedPreferences.edit().putLong("current_streak", currentStreak).apply() // Save current streak
                    handler.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }

    private fun resetStreak(streakDaysTextView: TextView, currentStreakTextView: TextView, longestStreakTextView: TextView) {
        isRunning = false
        if (currentStreak > longestStreak) {
            longestStreak = currentStreak
            sharedPreferences.edit().putLong("longest_streak", longestStreak).apply()
        }
        currentStreak = 0
        sharedPreferences.edit().putLong("current_streak", currentStreak).apply()
        streakDaysTextView.text = "0"
        currentStreakTextView.text = "Streak Sekarang: 0s"
        longestStreakTextView.text = "Streak Terlama: ${formatTime(longestStreak)}"
        isRunning = true
        startStreakTimer(streakDaysTextView, currentStreakTextView)
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        startStreakTimer(view?.findViewById(R.id.streak_days)!!, view?.findViewById(R.id.current_streak)!!)
    }

    private fun formatTime(seconds: Long): String {
        val hours = (seconds / 3600) % 24
        val minutes = (seconds / 60) % 60
        val secs = seconds % 60
        return String.format("%02dh %02dm %02ds", hours, minutes, secs)
    }
} 