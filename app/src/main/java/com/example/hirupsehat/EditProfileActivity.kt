package com.example.hirupsehat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    // Firebase Instance
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var currentUser: FirebaseUser

    // Input Field References
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        // Initialize Views
        nameEditText = findViewById(R.id.input_name)
        phoneEditText = findViewById(R.id.input_phone)
        bioEditText = findViewById(R.id.input_bio)
        birthdateEditText = findViewById(R.id.input_birthdate)
        saveButton = findViewById(R.id.btn_save)

        // Load existing user data
        loadUserData()

        // Save button click listener
        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedPhone = phoneEditText.text.toString()
            val updatedBio = bioEditText.text.toString()
            val updatedBirthdate = birthdateEditText.text.toString()

            // Update user data
            updateUserData(updatedName, updatedPhone, updatedBio, updatedBirthdate)
        }
    }

    private fun loadUserData() {
        // Ambil UID user
        val userId = currentUser.uid

        database.child(userId).get().addOnSuccessListener { snapshot ->
            nameEditText.setText(snapshot.child("name").value.toString())
            phoneEditText.setText(snapshot.child("phoneNumber").value.toString())
            bioEditText.setText(snapshot.child("bio").value.toString())
            birthdateEditText.setText(snapshot.child("birthdate").value.toString())
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserData(
        name: String,
        phone: String,
        bio: String,
        birthdate: String
    ) {
        val userId = currentUser.uid

        // Update data di database
        val userUpdates = mapOf(
            "name" to name,
            "phoneNumber" to phone,
            "bio" to bio,
            "birthdate" to birthdate
        )

        database.child(userId).updateChildren(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
            }
    }
}
