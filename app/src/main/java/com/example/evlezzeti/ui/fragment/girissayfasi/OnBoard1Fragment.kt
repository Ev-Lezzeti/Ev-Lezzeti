package com.example.evlezzeti.ui.fragment.girissayfasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.evlezzeti.R
import com.example.evlezzeti.databinding.FragmentOnBoard1Binding


class OnBoard1Fragment : Fragment() {
    private lateinit var binding: FragmentOnBoard1Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        binding = FragmentOnBoard1Binding.inflate(inflater,container,false)

        binding.button.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.onBoard2Fragment)
        }

        return binding.root
    }


}