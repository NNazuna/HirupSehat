package com.example.hirupsehat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var profileImageView: ImageView
    private lateinit var rankingTextView: TextView
    private lateinit var tvName: EditText
    private lateinit var tvPhone: EditText
    private lateinit var tvBio: EditText
    private lateinit var tvEmail: EditText
    private lateinit var tvBirthdate: EditText
    private lateinit var btnUpdateProfile: Button

    private val userId = "userId123" // Ganti dengan ID pengguna dinamis jika menggunakan Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance().getReference("HirupSehat/users")
        storage = FirebaseStorage.getInstance()

        // Inisialisasi Views
        profileImageView = findViewById(R.id.profile_image)
        rankingTextView = findViewById(R.id.tv_ranking)
        tvName = findViewById(R.id.tv_name)
        tvPhone = findViewById(R.id.tv_phone)
        tvBio = findViewById(R.id.tv_bio)
        tvEmail = findViewById(R.id.tv_email)
        tvBirthdate = findViewById(R.id.tv_birthdate)
        btnUpdateProfile = findViewById(R.id.btn_update_profile)

        // Load data pengguna
        loadUserData()

        // Aksi tombol "Perbarui Profil"
        btnUpdateProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserData() {
        database.child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val name = snapshot.child("name").value.toString()
                val phone = snapshot.child("phone").value.toString()
                val bio = snapshot.child("bio").value.toString()
                val email = snapshot.child("email").value.toString()
                val birthdate = snapshot.child("birthdate").value.toString()
                val ranking = snapshot.child("ranking").getValue(Int::class.java) ?: 1
                val profileImageUrl = snapshot.child("profileImage").value.toString()

                // Menampilkan data di EditText
                tvName.setText(name)
                tvPhone.setText(phone)
                tvBio.setText(bio)
                tvEmail.setText(email)
                tvBirthdate.setText(birthdate)

                // Menampilkan ranking
                rankingTextView.text = getString(R.string.ranking_text_placeholder, ranking)

                // Menampilkan foto profil menggunakan Glide
                Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.ic_user) // Gambar default
                    .into(profileImageView)
            }
        }.addOnFailureListener {
            // Handle error
            showToast(getString(R.string.error_loading_profile))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
