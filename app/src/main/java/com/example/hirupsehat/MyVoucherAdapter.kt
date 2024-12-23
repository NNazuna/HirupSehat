package com.example.hirupsehat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyVoucherAdapter(private val voucherList: List<PurchasedVoucher>) : RecyclerView.Adapter<MyVoucherAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val voucherName: TextView = view.findViewById(R.id.voucherName)
        val voucherStatus: TextView = view.findViewById(R.id.voucherStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_voucher, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = voucherList[position]
        holder.voucherName.text = voucher.name
        holder.voucherStatus.text = if (voucher.isActive) "Active" else "Inactive"
    }

    override fun getItemCount(): Int = voucherList.size
}
