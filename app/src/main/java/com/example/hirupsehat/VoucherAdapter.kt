package com.example.hirupsehat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hirupsehat.databinding.ItemVoucherBinding
import com.google.firebase.database.DatabaseReference

class VoucherAdapter(
    private val vouchers: List<Voucher>,
    private val database: DatabaseReference,
    private val userId: String
) : RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>() {

    inner class VoucherViewHolder(private val binding: ItemVoucherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(voucher: Voucher) {
            binding.textViewVoucherName.text = voucher.name
            binding.textViewVoucherPrice.text = "${voucher.price} Points"

            binding.buttonBuy.setOnClickListener {
                database.child("users").child(userId).get().addOnSuccessListener { snapshot ->
                    val currentPoints = snapshot.child("points").getValue(Int::class.java) ?: 0
                    if (currentPoints >= voucher.price) {
                        val newPoints = currentPoints - voucher.price
                        database.child("users").child(userId).child("points").setValue(newPoints)
                        database.child("users").child(userId).child("purchasedVouchers")
                            .child(voucher.name).setValue(true)
                        Toast.makeText(
                            binding.root.context,
                            "Voucher purchased successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Not enough points!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val binding = ItemVoucherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VoucherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(vouchers[position])
    }

    override fun getItemCount() = vouchers.size
}
