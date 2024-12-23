package com.example.hirupsehat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class VoucherSayaActivity : AppCompatActivity() {

    private lateinit var adapter: MyVoucherAdapter
    private val voucherList = ArrayList<PurchasedVoucher>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher_saya)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewVouchers)
        adapter = MyVoucherAdapter(voucherList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadPurchasedVouchers()
    }

    private fun loadPurchasedVouchers() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUID = currentUser?.uid

        if (currentUID != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users")

            databaseReference.child(currentUID).child("purchasedVouchers")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        voucherList.clear()

                        for (data in snapshot.children) {
                            val voucherName = data.key
                            val isActive = data.getValue(Boolean::class.java) ?: false

                            if (voucherName != null) {
                                voucherList.add(PurchasedVoucher(voucherName, isActive))
                            }
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@VoucherSayaActivity,
                            "Failed to fetch data: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
        }
    }
}
