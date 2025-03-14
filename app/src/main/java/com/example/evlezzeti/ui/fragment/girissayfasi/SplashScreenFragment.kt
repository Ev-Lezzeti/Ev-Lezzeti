package com.example.evlezzeti.ui.fragment.girissayfasi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.evlezzeti.BottomNavActivity
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth


class SplashScreenFragment : Fragment() {
    private val splashScreenDuration = 6000L // 8 saniye
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        val animation1 = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.animation_splash1)
        val animation2 = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.animation_splash2)

        // ImageView'lara animasyonları ata
        binding.splashImageGri.startAnimation(animation1)
        binding.splashImageRenkli.startAnimation(animation2)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            Toast.makeText(requireContext(),"Helal olsun",Toast.LENGTH_LONG).show()
            view?.let { Navigation.findNavController(it).navigate(R.id.splashToBottomNav) }
            val intent = Intent(requireContext(), BottomNavActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
        }
        else {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.onBoard1Fragment)
            }, splashScreenDuration)
        }


        return binding.root
    }

}