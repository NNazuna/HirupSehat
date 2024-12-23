package com.example.hirupsehat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        // Firebase Initialization
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        // Reference to UI elements
        val editProfile = view.findViewById<LinearLayout>(R.id.menu_edit_profile)
        val logout = view.findViewById<LinearLayout>(R.id.menu_logout)
        val setting = view.findViewById<LinearLayout>(R.id.menu_setting)
        userNameTextView = view.findViewById(R.id.title_profile)

        // Fetch current user data
        fetchUserData()

        // OnClickListener for Edit Profile
        editProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // OnClickListener for Logout
        logout.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        // OnClickListener for Settings
        setting.setOnClickListener {
            Toast.makeText(context, "Pengaturan belum diimplementasikan", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun fetchUserData() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val uid = user.uid
            database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    userNameTextView.text = name ?: "Profilku"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
