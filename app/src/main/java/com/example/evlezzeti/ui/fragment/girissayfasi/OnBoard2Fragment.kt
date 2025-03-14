package com.example.evlezzeti.ui.fragment.girissayfasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentOnBoard2Binding

class OnBoard2Fragment : Fragment() {
    private lateinit var binding: FragmentOnBoard2Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        binding = FragmentOnBoard2Binding.inflate(inflater, container, false)

        binding.button2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.onBoard3Fragment)
        }

        return binding.root
    }


}