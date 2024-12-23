package com.example.hirupsehat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyVoucherActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyVoucherAdapter
    private val purchasedVouchers = mutableListOf<PurchasedVoucher>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher_saya)

        recyclerView = findViewById(R.id.recyclerViewVouchers)
        adapter = MyVoucherAdapter(purchasedVouchers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadPurchasedVouchers()
    }

    private fun loadPurchasedVouchers() {
        purchasedVouchers.add(PurchasedVoucher("Voucher 1", true))
        purchasedVouchers.add(PurchasedVoucher("Voucher 2", false))

        adapter.notifyDataSetChanged()
    }
}
