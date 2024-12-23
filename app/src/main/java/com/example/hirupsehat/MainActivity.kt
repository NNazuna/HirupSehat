//Nama kelompok : KANI
//Anggota : Rafa Rafdhitya Riyanto ( 22523079 ), Anisah Fitri Maisaroh ( 22523130 ), Rizaldi Raditya Althaf (22523256), Dedhanof Ahnaf Faiz P. ( 22523301 )
//username : zae@gmail.com  password : 123qwe
package com.example.hirupsehat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil USER_ID dari intent
        userId = intent.getStringExtra("USER_ID")

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Navigasi antar fragment
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_user -> {
                    val userFragment = UserFragment()
                    val bundle = Bundle()
                    bundle.putString("USER_ID", userId)
                    userFragment.arguments = bundle
                    loadFragment(userFragment)
                    true
                }
                R.id.nav_gamifikasi -> {
                    loadFragment(GamifikasiFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .commit()
    }
}
