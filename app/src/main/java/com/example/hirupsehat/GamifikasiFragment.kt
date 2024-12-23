package com.example.hirupsehat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hirupsehat.databinding.FragmentGamifikasiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GamifikasiFragment : Fragment() {

    private var _binding: FragmentGamifikasiBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamifikasiBinding.inflate(inflater, container, false)
        val view = binding.root

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userId = currentUser.uid
        } else {
            binding.pointsText.text = "0"
            return view
        }

        database = FirebaseDatabase.getInstance().reference

        // Load user points dynamically
        loadUserPoints()

        // Setup click listeners
        binding.menuMission.setOnClickListener {
            val intent = Intent(activity, DailyMissionActivity::class.java)
            startActivity(intent)
        }

        binding.menuGame.setOnClickListener {
            binding.menuGame.setOnClickListener {
                val intent = Intent(activity, MiniGamesActivity::class.java)
                startActivity(intent)
            }

        }

        binding.menuVoucher.setOnClickListener {
            val intent = Intent(activity, VoucherActivity::class.java)
            startActivity(intent)
        }

        binding.menuVoucherSaya.setOnClickListener {
            val intent = Intent(context, VoucherSayaActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadUserPoints() {
        database.child("users").child(userId).child("points")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val points = snapshot.getValue(Int::class.java) ?: 0
                    binding.pointsText.text = points.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.pointsText.text = "0"
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
