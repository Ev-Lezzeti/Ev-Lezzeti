package com.example.evlezzeti

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.evlezzeti.databinding.ActivityBottomNavBinding
import com.example.evlezzeti.ui.viewmodel.SharedKullaniciViewModel
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomNavBinding
    private val sharedKullaniciViewModel: SharedKullaniciViewModel by viewModels()

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

        //Giris ve Splash ekranlarindan gelen bilgileri burada kaydettik
        val kullaniciId = intent.getStringExtra("kullaniciId")
        val kullaniciEPosta = intent.getStringExtra("kullaniciEPosta")
        sharedKullaniciViewModel.kullaniciIdGuncelle(kullaniciId ?: "")
        sharedKullaniciViewModel.kullaniciEpostaGuncelle(kullaniciEPosta ?: "")

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val navOptions = androidx.navigation.navOptions {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = false
                    inclusive = false
                }
                launchSingleTop = true
                restoreState = false
            }

            when (item.itemId) {
                R.id.bottomNavMenuFragment -> {
                    navController.navigate(R.id.bottomNavMenuFragment, null, navOptions)
                    true
                }
                R.id.bottomNavSepetToBottomNavMenuFragment -> {
                    navController.navigate(R.id.bottomNavSepetToBottomNavMenuFragment, null, navOptions)
                    true
                }
                R.id.bottomNavProfilFragment -> {
                    navController.navigate(R.id.bottomNavProfilFragment, null, navOptions)
                    true
                }
                else -> false
            }
        }


    }
}