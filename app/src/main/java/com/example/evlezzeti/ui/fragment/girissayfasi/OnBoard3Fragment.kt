package com.example.evlezzeti.ui.fragment.girissayfasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentOnBoard3Binding

class OnBoard3Fragment : Fragment() {
    private lateinit var binding: FragmentOnBoard3Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        binding = FragmentOnBoard3Binding.inflate(inflater,container,false)
        binding.button3.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.girisYapFragment)
        }
        return binding.root
    }


}