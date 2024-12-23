package com.example.hirupsehat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirupsehat.databinding.ActivityVoucherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VoucherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoucherBinding
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoucherBinding.inflate(layoutInflater)
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

        val vouchers = listOf(
            Voucher("Membership Gym", 3000),
            Voucher("Healthy Snack", 1500),
            Voucher("Discount Vitamin", 2000),
            Voucher("Healthy Beverage", 2500),
            Voucher("Sport Equipment Discount", 3500)
        )

        setupRecyclerView(vouchers)
    }

    private fun setupRecyclerView(vouchers: List<Voucher>) {
        val adapter = VoucherAdapter(vouchers, database, userId)
        binding.recyclerViewVouchers.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewVouchers.adapter = adapter
    }
}
