package com.example.evlezzeti

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.evlezzeti.databinding.ActivityBottomNavBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // BottomNav icin kurulum
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navHostFragment.navController)

        //Diger bottomNavFragment'larda gezerken geri donus yaptiginde ilk baslangic fragmentina donuyor
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottomNavMenuFragment -> {
                    navController.navigate(R.id.bottomNavMenuFragment)
                    true
                }
                R.id.bottomNavSepetToBottomNavMenuFragment -> {
                    navController.navigate(R.id.bottomNavSepetToBottomNavMenuFragment)
                    true
                }
                R.id.bottomNavProfilFragment -> {
                    navController.navigate(R.id.bottomNavProfilFragment)
                    true
                }
                else -> false
            }
        }

    }
}